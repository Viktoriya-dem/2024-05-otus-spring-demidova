package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcBookRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            GenreRepository genreRepository) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.genreRepository = genreRepository;
    }

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Book book = namedParameterJdbcTemplate.query("select b.id as book_id, b.title as book_title, " +
                                                     "a.id as author_id, a.full_name as author_full_name, " +
                                                     "g.id as genre_id, g.name as genre_name " +
                                                     "from books b " +
                                                     "left join authors a on b.author_id = a.id " +
                                                     "left join books_genres bg on bg.book_id = b.id " +
                                                     "left join genres g on g.id = bg.genre_id " +
                                                     "where b.id = :id", params, new BookResultSetExtractor());

        if (book != null && book.getId() != 0) {
            return Optional.of(book);
        }

        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedParameterJdbcTemplate.update("delete from books where id = :id", params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        return namedParameterJdbcTemplate.query("select b.id, b.title, a.id as author_id, a.full_name " +
                                                "from books b " +
                                                "left join authors a on b.author_id = a.id ",
                new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return namedParameterJdbcTemplate.query(
                "select book_id, genre_id from books_genres bg order by book_id, genre_id",
                (rs, i) -> new BookGenreRelation(rs.getLong(1), rs.getLong(2)));
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
            List<BookGenreRelation> relations) {

        Map<Long, Genre> genresMap = genres.stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
        Map<Long, Book> booksWithoutGenresMap =
                booksWithoutGenres.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
        relations.forEach(r -> {
            if (booksWithoutGenresMap.containsKey(r.bookId()) && genresMap.containsKey(r.genreId())) {
                booksWithoutGenresMap.get(r.bookId()).getGenres().add(genresMap.get(r.genreId()));
            }
        });

        for (Book book: booksWithoutGenres) {
            book.setGenres(booksWithoutGenresMap.get(book.getId()).getGenres());
        }
    }

    @SuppressWarnings({"DataFlowIssue"})
    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", book.getId());
        namedParameters.addValue("title", book.getTitle());
        namedParameters.addValue("author_id", book.getAuthor().getId());

        namedParameterJdbcTemplate.update("insert into books (title, author_id) values (:title, :author_id) ",
                namedParameters, keyHolder, new String[] {"id"});

        book.setId(keyHolder.getKey().longValue());

        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        int count = namedParameterJdbcTemplate.update("UPDATE books " +
                                                      "SET title = :title, " +
                                                      "author_id = :author_id " +
                                                      "where id=:id ",
                Map.of("id", book.getId(), "title", book.getTitle(), "author_id", book.getAuthor().getId()));

        if (count == 0) {
            throw new EntityNotFoundException(String.format("Error update: book id %s not found", book.getId()));
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        MapSqlParameterSource[] mapSqlParameterSources = new MapSqlParameterSource[book.getGenres().size()];

        for (int i = 0; i < book.getGenres().size(); i++) {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("book_id", book.getId());
            namedParameters.addValue("genre_id", book.getGenres().get(i).getId());
            mapSqlParameterSources[i] = namedParameters;
        }

        namedParameterJdbcTemplate.batchUpdate("INSERT INTO books_genres (book_id, genre_id) " +
                                               "VALUES(:book_id, :genre_id)", mapSqlParameterSources);
    }

    private void removeGenresRelationsFor(Book book) {
        Map<String, Object> params = Collections.singletonMap("id", book.getId());
        namedParameterJdbcTemplate.update("delete from books_genres " +
                                          "where book_id = :id", params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            long authorId = rs.getLong("author_id");
            String authorFullName = rs.getString("full_name");
            book.setAuthor(new Author(authorId, authorFullName));
            book.setGenres(new ArrayList<>());

            return book;
        }
    }


    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = new Book();
            book.setGenres(new ArrayList<>());

            while (rs.next()) {
                long bookId = rs.getLong("book_id");
                long authorId = rs.getLong("author_id");
                String bookTitle = rs.getString("book_title");
                String authorFullName = rs.getString("author_full_name");
                long genreId = rs.getLong("genre_id");
                String genreName = rs.getString("genre_name");
                book.setId(bookId);
                book.setTitle(bookTitle);
                book.setAuthor(new Author(authorId, authorFullName));
                book.getGenres().add(new Genre(genreId, genreName));
            }

            return book;
        }
    }


    private record BookGenreRelation(long bookId, long genreId) {
    }
}

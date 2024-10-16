package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.mappers.BookMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoFullInfo;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDtoFullInfo> findAll() {
        List<Book> books = bookRepository.findAll();

        for (Book book : books) {
            book.getGenres().size();
        }

        return bookMapper.toDtoFullInfo(books);
    }

    @Override
    @Transactional
    public BookDto create(BookDto bookDto) {
        bookDto.setId(0L);
        Book book = bookMapper.toEntity(bookDto, getAuthor(bookDto.getAuthorId()), getGenres(bookDto.getGenres()));

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(BookDto bookDto) {
        Optional<Book> optionalBook = bookRepository.findById(bookDto.getId());

        if (optionalBook.isPresent()) {
            Book book = bookMapper.toEntity(bookDto, getAuthor(bookDto.getAuthorId()), getGenres(bookDto.getGenres()));

            return bookMapper.toDto(bookRepository.save(book));
        } else {
            throw new NotFoundException("Book not found");
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Author getAuthor(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private List<Genre> getGenres(Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        List<Genre> genres = genreRepository.findAllByIdIn(genresIds);

        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new NotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        return genres;
    }
}

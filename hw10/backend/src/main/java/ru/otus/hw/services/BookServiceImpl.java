package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.BookMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public BookDto findById(UUID id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        List<Book> books = bookRepository.findAll();

        return bookMapper.toDto(books);
    }

    @Override
    @Transactional
    public BookDto create(BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto, getAuthor(bookDto.getAuthor().getId()), getGenres(bookDto.getGenres()));

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookDto update(BookDto bookDto) {
        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %s not found".formatted(bookDto.getId())));
        book.setTitle(bookDto.getTitle());
        book.setAuthor(getAuthor(bookDto.getAuthor().getId()));
        book.setGenres(getGenres(bookDto.getGenres()));

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        bookRepository.deleteById(id);
    }

    private Author getAuthor(UUID authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %s not found".formatted(authorId)));
    }

    private List<Genre> getGenres(Set<GenreDto> genresDto) {
        if (isEmpty(genresDto)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        List<Genre> genres = genreRepository.findAllByIdIn(genresDto.stream().map(GenreDto::getId).collect(Collectors.toSet()));

        if (isEmpty(genres) || genresDto.size() != genres.size()) {
            throw new NotFoundException("One or all genres with ids %s not found".formatted(genresDto));
        }

        return genres;
    }
}

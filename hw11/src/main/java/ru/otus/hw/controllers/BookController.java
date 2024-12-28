package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    @GetMapping("/api/books")
    public Flux<BookDto> getAllBooks() {

        return bookRepository.findAll().map(bookMapper::toDto);
    }

    @GetMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> getBookById(@PathVariable UUID id) {

        return bookRepository.findById(id).map(bookMapper::toDto).map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books")
    public Mono<ResponseEntity<BookDto>> createBook(@RequestBody @Valid BookCreateDto bookDto) {

        return save(bookDto, bookDto.getAuthor().getId(),
                bookDto.getGenres().stream().map(GenreDto::getId).toList())
                .map(b -> new ResponseEntity<>(bookMapper.toDto(b), HttpStatus.CREATED))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PatchMapping("/api/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<BookDto>> updateBook(@PathVariable UUID id, @RequestBody @Valid BookUpdateDto bookDto) {

        bookDto.setId(id);

        return update(bookDto, bookDto.getAuthor().getId(),
                bookDto.getGenres().stream().map(GenreDto::getId).toList())
                .map(b -> new ResponseEntity<>(bookMapper.toDto(b), HttpStatus.OK))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable UUID id) {

        return bookRepository.deleteById(id)
                .then(Mono.fromCallable(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    private Mono<Book> save(BookCreateDto bookCreateDto, UUID authorId, List<UUID> genresIds) {
        var genresFlux = genreRepository.findAllById(genresIds).collectList();
        var authorMono = authorRepository.findById(authorId);

        return Mono.zip(genresFlux, authorMono, (genres, author) -> new Book(bookCreateDto.getId(),
                        bookCreateDto.getTitle(), author, genres))
                .flatMap(bookRepository::save);
    }

    private Mono<Book> update(BookUpdateDto bookUpdateDto, UUID authorId, List<UUID> genresIds) {
        var genresFlux = genreRepository.findAllById(genresIds).collectList();
        var authorMono = authorRepository.findById(authorId);

        return Mono.zip(genresFlux, authorMono,
                        (genres, author) -> new Book(bookUpdateDto.getId(), bookUpdateDto.getTitle(),
                                author, genres))
                .flatMap(bookRepository::save);
    }

}

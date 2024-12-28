package ru.otus.hw.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.CommentMapperImpl;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;

@DisplayName("Контроллер книг должен ")
@ExtendWith(SpringExtension.class)
@Import({BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class, CommentMapperImpl.class})
@WebFluxTest(controllers = BookController.class)
class BookControllerTest {

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookMapper mapper;

    BookMapper bookMapper;

    GenreMapper genreMapper;

    AuthorMapper authorMapper;


    @BeforeEach
    void setup() {
        bookMapper = Mappers.getMapper(BookMapper.class);
        genreMapper = Mappers.getMapper(GenreMapper.class);
        authorMapper = Mappers.getMapper(AuthorMapper.class);
    }

    @DisplayName("вернуть все книги")
    @Test
    void shouldReturnAllBooks() {
        var author = getAuthor();
        var genres = getGenres();
        var books = List.of(
                new Book(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                        "Book_Title_1", author, genres),
                new Book(UUID.fromString("f7b16ec4-3e96-4693-b761-db978faf0087"),
                        "Book_Title_2", author, genres)
        );
        BDDMockito.given(bookRepository.findAll()).willReturn(Flux.fromIterable(books));

        var result = webTestClient.get().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = null;
        for (Book book : books) {
            stepResult = step.expectNext(mapper.toDto(book));
        }

        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("вернуть книгу по id")
    @Test
    void shouldReturnBookById() {
        var author = getAuthor();
        var genres = getGenres();
        var book = new Book(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                "Book_Title_1", author, genres);
        BDDMockito.given(bookRepository.findById(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848")))
                .willReturn(Mono.just(book));

        var result = webTestClient.get().uri("/api/books/%s".formatted(book.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(mapper.toDto(book));
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("вернуть 404 при поиске несуществующей книги")
    @Test
    void shouldReturnNotFoundWhenNoBook() {
        BDDMockito.given(bookRepository.findById(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848")))
                .willThrow(EntityNotFoundException.class);

        webTestClient.get()
                .uri("/api/books/%s".formatted(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848")))
                .exchange()
                .expectStatus().isNotFound();
    }

    @DisplayName("создать новую книгу")
    @Test
    void shouldCreateNewBook() {
        var author = getAuthor();
        var genres = getGenres();
        var createdBook = new Book(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                "BookTitle_4", author, genres);
        var createBookDto = new BookDto(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                "BookTitle_4", authorMapper.toDto(author),
                genreMapper.toDto(genres));
        BDDMockito.given(authorRepository.findById((UUID) any()))
                .willReturn(Mono.just(author));
        BDDMockito.given(bookRepository.save(any())).willReturn(Mono.just(createdBook));
        BDDMockito.given(genreRepository.findAllById(anyIterable())).willReturn(Flux.fromIterable(genres));

        var result = webTestClient
                .post().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createBookDto)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(createBookDto);
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("обновить книгу")
    @Test
    void shouldUpdateBook() {
        var author = getAuthor();
        var genres = getGenres();
        var updatedBook = new Book(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                "BookTitle_4", author, genres);
        var updatedBookDto = new BookDto(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                "BookTitle_4", authorMapper.toDto(author),
                genreMapper.toDto(genres));
        BDDMockito.given(authorRepository.findById((UUID) any()))
                .willReturn(Mono.just(author));
        BDDMockito.given(genreRepository.findAllById(anyIterable())).willReturn(Flux.fromIterable(genres));
        BDDMockito.given(bookRepository.save(any())).willReturn(Mono.just(updatedBook));

        var result = webTestClient
                .patch().uri("/api/books/%s".formatted(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848")))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBookDto)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        StepVerifier.Step<BookDto> stepResult = step.expectNext(updatedBookDto);
        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }

    @DisplayName("удалить книгу")
    @Test
    void shouldDeleteBook() {
        BDDMockito.given(bookRepository.deleteById(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"))).willReturn(Mono.empty());

        var result = webTestClient
                .delete().uri("/api/books/%s".formatted(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848")))
                .exchange()
                .expectStatus().isNoContent()
                .returnResult(Void.class)
                .getResponseBody();

        var step = StepVerifier.create(result);
        step.verifyComplete();
    }

    public List<Genre> getGenres() {
        return List.of(new Genre(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"),
                "Genre_1"));
    }

    public Author getAuthor() {
        return new Author(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "Author_1");
    }
}

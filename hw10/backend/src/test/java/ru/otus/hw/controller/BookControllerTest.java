package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;
import org.springframework.http.MediaType;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.BookMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер книг должен ")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    BookMapper bookMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    void setup() {
        bookMapper = Mappers.getMapper(BookMapper.class);
    }

    Author author = new Author(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "Author_1");
    List<Genre> genres = List.of(new Genre(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"),
            "Genre_1"));

    @DisplayName("вернуть все книги")
    @Test
    void shouldReturnAllBook() throws Exception {
        var bookDtos = List.of(
                getBookDto1(),
                getBookDto2()
        );

        when(bookService.findAll()).thenReturn(bookDtos);

        mockMvc.perform(get("/api/books"))
                .andExpect(content().json(mapper.writeValueAsString(bookDtos)))
                .andExpect(status().isOk());
    }

    @DisplayName("создать книгу")
    @Test
    void shouldCreateBook() throws Exception {
        BookDto bookDto = getBookDto1();
        BookCreateDto bookCreateDto = new BookCreateDto(bookDto.getId(),
                bookDto.getTitle(), bookDto.getAuthor(), bookDto.getGenres());

        when(bookService.create(bookCreateDto)).thenReturn(bookDto);

        var content = post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto));


        mockMvc.perform(content)
                .andExpect(status().isCreated());
    }

    @DisplayName("обновить книгу")
    @Test
    void shouldUpdateBook() throws Exception {
        BookDto bookDto = getBookDto1();
        BookUpdateDto bookUpdateDto = new BookUpdateDto(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                "Book_Title_1", bookDto.getAuthor(), bookDto.getGenres());

        when(bookService.update(bookUpdateDto)).thenReturn(bookDto);

        var content = patch("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto));

        mockMvc.perform(content)
                .andExpect(status().isOk());
    }

    @DisplayName("удалить книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        doNothing().when(bookService);

        var content = delete("/api/books/%s"
                .formatted("8b0f427f-1365-4883-8834-c6b25515b848"));

        mockMvc.perform(content)
                .andExpect(status().isNoContent());
    }

    @DisplayName("вернуть ошибку 404 при неверном id книги")
    @Test
    void shouldNotUpdateBookWhenWrongBookId() throws Exception {
        BookDto bookDto = getBookDto1();
        BookUpdateDto bookUpdateDto = new BookUpdateDto(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                "Book_Title_1", bookDto.getAuthor(), bookDto.getGenres());

        when(bookService.update(bookUpdateDto)).thenThrow(new NotFoundException("Book not found"));

        var content = patch("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto));

        mockMvc.perform(content)
                .andExpect(status().isNotFound());
    }

    @DisplayName("вернуть ошибку 400 при отсутствии id автора")
    @Test
    void shouldNotUpdateBookWhenWrongAuthorId() throws Exception {
        BookDto bookDto = getBookDto1();
        bookDto.setId(null);
        BookUpdateDto bookUpdateDto = new BookUpdateDto(null,
                "Book_Title_1", bookDto.getAuthor(), bookDto.getGenres());

        when(bookService.update(bookUpdateDto)).thenReturn(bookDto);

        var content = patch("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto));

        mockMvc.perform(content)
                .andExpect(status().isBadRequest());
    }

    public BookDto getBookDto1() {
        return new BookDto(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"), "Book_Title_1",
                new AuthorDto(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "Author_1"),
                Set.of(new GenreDto(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"), "Genre_1")));
    }

    public BookDto getBookDto2() {
        return new BookDto(UUID.fromString("f7b16ec4-3e96-4693-b761-db978faf0087"), "Book_Title_2",
                new AuthorDto(UUID.fromString("30df0652-0b5d-40af-86d9-cd336b836648"), "Author_2"),
                Set.of(new GenreDto(UUID.fromString("66f51d2d-e6b7-4602-8b21-31439ea1f721"), "Genre3"),
                        new GenreDto(UUID.fromString("dc511c5a-1436-4b96-b727-3c88b1e423d4"), "Genre4")));
    }

}
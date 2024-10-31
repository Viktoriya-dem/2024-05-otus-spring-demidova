package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;
import org.springframework.http.MediaType;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;

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

    Author author = new Author(1L, "Author_1");
    List<Genre> genres = List.of(new Genre(1L, "Genre_1"));

    @DisplayName("вернуть все книги")
    @Test
    void shouldReturnAllBook() throws Exception {
        var bookDtos = List.of(
                getBookDto1(),
                getBookDto2()
        );

        when(bookService.findAll()).thenReturn(bookDtos);

        mockMvc.perform(get("/books"))
                .andExpect(content().json(mapper.writeValueAsString(bookDtos)))
                .andExpect(status().isOk());
    }

    @DisplayName("создать книгу")
    @Test
    void shouldCreateBook() throws Exception {
        BookDto bookDto = getBookDto1();

        when(bookService.create(bookDto)).thenReturn(bookDto);

        var content = post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto));


        mockMvc.perform(content)
                .andExpect(status().isCreated());
    }

    @DisplayName("обновить книгу")
    @Test
    void shouldUpdateBook() throws Exception {
        BookDto bookDto = getBookDto1();
        Book book = new Book(1L, "Book_Title_1", author, genres);

        when(bookService.update(bookDto)).thenReturn(bookDto);

        var content = patch("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookDto));

        mockMvc.perform(content)
                .andExpect(status().isOk());
    }

    @DisplayName("удалить книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        doNothing().when(bookService);

        var content = delete("/books/%d" .formatted(1));

        mockMvc.perform(content)
                .andExpect(status().isNoContent());
    }

    public BookDto getBookDto1() {
        return new BookDto(1L, "Book_Title_1", new AuthorDto(1L, "Author_1"),
                Set.of(new GenreDto(1L, "Genre_1")));
    }

    public BookDto getBookDto2() {
        return new BookDto(2L, "Book_Title_2", new AuthorDto(2L, "Author_2"),
                Set.of(new GenreDto(3L, "Genre3"), new GenreDto(4L, "Genre4")));
    }

}
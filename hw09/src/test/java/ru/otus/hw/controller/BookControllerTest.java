package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;
import ru.otus.hw.dto.BookDtoFullInfo;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Контроллер книг должен ")
@WebMvcTest(BookController.class)
class BookControllerTest {

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
        var books = List.of(
                new BookDtoFullInfo(1L, "Book1 Title", "Author1", "Genre1"),
                new BookDtoFullInfo(2L, "Book2 Title", "Author2", "Genre2")
        );

        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/books/all"))
                .andExpect(view().name("books/book-list"))
                .andExpect(model().attribute("books", books))
                .andExpect(status().isOk());
    }

    @DisplayName("создать книгу")
    @Test
    void shouldCreateBook() throws Exception {
        BookDto bookDto = new BookDto(null, "Title_1", 1L, Set.of(1L));
        Book book = new Book(1L, "Title_1", author, genres);

        when(bookService.create(bookDto)).thenReturn(bookMapper.toDto(book));

        mockMvc.perform(post("/books/create").flashAttr("book", bookDto))
                .andExpect(redirectedUrl("/books/all"));
    }

    @DisplayName("обновить книгу")
    @Test
    void shouldUpdateBook() throws Exception {
        BookDto bookDto = new BookDto(1L, "Title_1", 1L, Set.of(1L));
        Book book = new Book(1L, "Title_1", author, genres);

        when(bookService.update(bookDto)).thenReturn(bookMapper.toDto(book));

        mockMvc.perform(post("/books/edit").flashAttr("book", bookDto))
                .andExpect(redirectedUrl("/books/all"));
    }

    @DisplayName("удалить книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        doNothing().when(bookService);
        mockMvc.perform(post("/books/delete")
                        .param("id", "1"))
                .andExpect(redirectedUrl("/books/all"));
    }

}
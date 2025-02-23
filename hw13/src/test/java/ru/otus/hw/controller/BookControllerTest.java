package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoFullInfo;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Контроллер книг должен ")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerTest {

    BookMapper bookMapper;
    Author author = new Author(1L, "Author_1");
    List<Genre> genres = List.of(new Genre(1L, "Genre_1"));
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        bookMapper = Mappers.getMapper(BookMapper.class);
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("вернуть первую книгу")
    @Order(1)
    @Test
    void shouldReturnFirstBook() throws Exception {
        var books = List.of(
                new BookDtoFullInfo(1L, "BookTitle_1", "Author_1", "Genre_1,Genre_2")
        );

        mockMvc.perform(get("/books/all"))
                .andExpect(view().name("books/book-list"))
                .andExpect(model().attribute("books", books))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("создать книгу")
    @Order(2)
    @Test
    void shouldCreateBook() throws Exception {
        BookDto bookDto = new BookDto(null, "Title_1", 1L, Set.of(1L));
        Book book = new Book(1L, "Title_1", author, genres);

        mockMvc.perform(post("/books/create").with(csrf()).flashAttr("book", bookDto))
                .andExpect(redirectedUrl("/books/all"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("не обновить книгу из-за отсутствия прав")
    @Order(3)
    @Test
    void shouldNotUpdateBook() throws Exception {
        BookDto bookDto = new BookDto(1L, "Title_1", 1L, Set.of(1L));
        Book book = new Book(1L, "Title_1", author, genres);

        mockMvc.perform(post("/books/edit").with(csrf()).flashAttr("book", bookDto))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("обновить книгу")
    @Order(4)
    @Test
    void shouldUpdateBook() throws Exception {
        BookDto bookDto = new BookDto(2L, "Title_2", 2L, Set.of(2L));

        mockMvc.perform(post("/books/edit").with(csrf()).flashAttr("book", bookDto))
                .andExpect(redirectedUrl("/books/all"));
    }

    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @DisplayName("не удалить книгу из-за отсутствия прав")
    @Order(5)
    @Test
    void shouldNotDeleteBook() throws Exception {
        mockMvc.perform(post("/books/delete")
                        .param("id", "2")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName(" удалить книгу")
    @Order(6)
    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(post("/books/delete")
                        .param("id", "2")
                        .with(csrf()))
                .andExpect(redirectedUrl("/books/all"));
    }

}
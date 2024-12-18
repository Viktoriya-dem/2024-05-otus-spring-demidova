package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.AuthorController;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
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
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер авторов должен ")
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;


    AuthorMapper authorMapper;

    @MockBean
    private AuthorService authorService;


    @BeforeEach
    void setup() {
        authorMapper = Mappers.getMapper(AuthorMapper.class);
    }


    @DisplayName("вернуть всех авторов")
    @Test
    void shouldReturnAllAuthors() throws Exception {
        var authorDtos = List.of(
                new AuthorDto(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "Author_1"),
                new AuthorDto(UUID.fromString("30df0652-0b5d-40af-86d9-cd336b836648"), "Author_2")
        );

        when(authorService.findAll()).thenReturn(authorDtos);

        mockMvc.perform(get("/api/authors"))
                .andExpect(content().json(mapper.writeValueAsString(authorDtos)))
                .andExpect(status().isOk());
    }

}
package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.AuthorController;
import ru.otus.hw.controllers.GenreController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер жанров должен ")
@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;


    GenreMapper genreMapper;

    @MockBean
    private GenreService genreService;


    @BeforeEach
    void setup() {
        genreMapper = Mappers.getMapper(GenreMapper.class);
    }


    @DisplayName("вернуть все жанры")
    @Test
    void shouldReturnAllGenres() throws Exception {
        var genreDtos = List.of(
                new GenreDto(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"), "Genre_1"),
                new GenreDto(UUID.fromString("980fab3b-338d-45e7-83b6-29b98d1c4b02"), "Genre_2")
        );

        when(genreService.findAll()).thenReturn(genreDtos);

        mockMvc.perform(get("/api/genres"))
                .andExpect(content().json(mapper.writeValueAsString(genreDtos)))
                .andExpect(status().isOk());
    }

}
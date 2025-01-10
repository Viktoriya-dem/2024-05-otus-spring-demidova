package ru.otus.hw.controller;

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
import reactor.test.StepVerifier;
import ru.otus.hw.controllers.GenreController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Контроллер жанров должен ")
@ExtendWith(SpringExtension.class)
@Import(GenreMapperImpl.class)
@WebFluxTest(controllers = GenreController.class)
class GenreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private GenreMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(GenreMapper.class);
    }

    @DisplayName("вернуть все жанры")
    @Test
    void shouldReturnAllGenres() throws Exception {
        var genres = List.of(
                new Genre(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"), "Genre_1"),
                new Genre(UUID.fromString("980fab3b-338d-45e7-83b6-29b98d1c4b02"), "Genre_2")
        );
        BDDMockito.given(genreRepository.findAll()).willReturn(Flux.fromIterable(genres));

        var result = webTestClient.get().uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(GenreDto.class)
                .getResponseBody();
        var step = StepVerifier.create(result);
        StepVerifier.Step<GenreDto> stepResult = null;
        for (var genre : genres) {
            stepResult = step.expectNext(mapper.toDto(genre));
        }

        assertThat(stepResult).isNotNull();
        stepResult.verifyComplete();
    }
}

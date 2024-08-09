package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;

import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с жанрами ")
@DataMongoTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenresByIds() {
        val expectedGenres = IntStream.range(1, 3).boxed()
                .map(id -> mongoTemplate.findAll(Genre.class))
                .toList();
        val actualGenres = genreRepository.findAllByIdIn(Set.of("1", "2"));

        assertThat(actualGenres.size()).isEqualTo(expectedGenres.size());
        assertThat(actualGenres).hasOnlyElementsOfType(Genre.class);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = genreRepository.findAll();
        var expectedGenres = mongoTemplate.findAll(Genre.class);

        assertThat(actualGenres.size()).isEqualTo(expectedGenres.size());
        assertThat(actualGenres).hasOnlyElementsOfType(Genre.class);
    }

}
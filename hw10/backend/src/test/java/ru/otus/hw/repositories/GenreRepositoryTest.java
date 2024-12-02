package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenresByIds() {
        val expectedGenres = getDbGenres().subList(0, 3);
        val actualGenres = genreRepository.findAllByIdIn(Set.of(UUID
                        .fromString("9fccd731-27a2-4639-b1f6-648087ef744b"),
                UUID.fromString("980fab3b-338d-45e7-83b6-29b98d1c4b02"),
                UUID.fromString("66f51d2d-e6b7-4602-8b21-31439ea1f721")));

        assertThat(actualGenres.stream().sorted(Comparator.comparing(Genre::getName)).collect(Collectors.toList()))
                .containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualAuthors = genreRepository.findAll();
        var expectedAuthors = getDbGenres();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    private List<Genre> getDbGenres() {
        return List.of(em.find(Genre.class, UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b")),
                em.find(Genre.class, UUID.fromString("980fab3b-338d-45e7-83b6-29b98d1c4b02")),
                em.find(Genre.class, UUID.fromString("66f51d2d-e6b7-4602-8b21-31439ea1f721")),
                em.find(Genre.class, UUID.fromString("dc511c5a-1436-4b96-b727-3c88b1e423d4")),
                em.find(Genre.class, UUID.fromString("a15f63d1-8cb3-477a-a5ab-b1fc0ea00fd4")),
                em.find(Genre.class, UUID.fromString("03ce09ef-f30a-4459-867f-1705cce28da6"))
                );
    }
}
package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
@Import({JpaGenreRepository.class})
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenresByIds() {
        val expectedGenres = getDbGenres().subList(0, 3);
        val actualGenres = jpaGenreRepository.findAllByIds(Set.of(1L, 2L, 3L));

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualAuthors = jpaGenreRepository.findAll();
        var expectedAuthors = getDbGenres();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    private List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> em.find(Genre.class, id))
                .toList();
    }
}
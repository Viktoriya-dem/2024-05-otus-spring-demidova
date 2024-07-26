package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataJpaTest
@Import({JpaAuthorRepository.class})
class JpaAuthorRepositoryTest {

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        val expectedAuthor = em.find(Author.class, 1L);
        val actualAuthor = jpaAuthorRepository.findById(1L);
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = jpaAuthorRepository.findAll();
        var expectedAuthors = getDbAuthors();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    private List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> em.find(Author.class, id))
                .toList();
    }
}
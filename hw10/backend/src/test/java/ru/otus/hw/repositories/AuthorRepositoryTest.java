package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        val expectedAuthor = em.find(Author.class, UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"));
        val actualAuthor = authorRepository
                .findById(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"));
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = authorRepository.findAll();
        var expectedAuthors = getDbAuthors();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    private List<Author> getDbAuthors() {
        return  List.of(em.find(Author.class, UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252")),
                em.find(Author.class, UUID.fromString("30df0652-0b5d-40af-86d9-cd336b836648")),
                em.find(Author.class, UUID.fromString("57daec39-1f36-4248-abaf-9e12bb9e77f5"))
        );
    }
}
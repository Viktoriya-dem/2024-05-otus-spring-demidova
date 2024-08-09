package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами ")
@DataMongoTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        val expectedAuthor = mongoTemplate.findById("1", Author.class);
        val actualAuthor = authorRepository.findById("1");
        assertThat(actualAuthor).isPresent()
                .get()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = authorRepository.findAll();
        var expectedAuthors = mongoTemplate.findAll(Author.class);
        ;

        assertThat(actualAuthors.size()).isEqualTo(expectedAuthors.size());
        assertThat(actualAuthors).hasOnlyElementsOfType(Author.class);

        actualAuthors.forEach(System.out::println);
    }

}
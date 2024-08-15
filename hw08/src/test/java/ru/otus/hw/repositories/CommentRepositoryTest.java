package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с комментариями ")
@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен загружать комментарий по id")
    @Test
    @Order(1)
    void shouldReturnCorrectCommentById() {
        val expectedComment = mongoTemplate.findById("1", Comment.class);
        val actualComment = commentRepository.findById("1");
        assertThat(actualComment).isPresent();
        assertThat(actualComment.get().getId()).isEqualTo(expectedComment.getId());
        assertThat(actualComment.get().getText()).isEqualTo(expectedComment.getText());
        assertThat(actualComment.get().getBook().getId()).isEqualTo(expectedComment.getBook().getId());
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    @Order(2)
    void shouldReturnCorrectCommentsListByBookId() {
        var actualComments = commentRepository.findAllByBookId("1");
        var expectedComments = mongoTemplate.findAll(Comment.class).stream()
                .filter(comment -> Objects.equals(comment.getBook().getId(), "1")).toList();

        assertThat(actualComments.size()).isEqualTo(expectedComments.size());
        assertThat(actualComments).hasOnlyElementsOfType(Comment.class);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    @Order(3)
    void shouldSaveNewComment() {
        var expectedComment = new Comment("10", "CommentText_X", getDbBook());
        var returnedComment = commentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> !comment.getId().isBlank())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    @Order(4)
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment("1", "CommentText_X", getDbBook());

        assertThat(commentRepository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedBook = commentRepository.save(expectedComment);
        assertThat(returnedBook).isNotNull()
                .matches(book -> !book.getId().isBlank())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    @Order(5)
    void shouldDeleteComment() {
        assertThat(commentRepository.findById("1")).isPresent();
        commentRepository.deleteById("1");
        assertThat(mongoTemplate.findById("1", Comment.class)).isNull();
    }


    private Book getDbBook() {
        return mongoTemplate.findById("1", Book.class);
    }

}
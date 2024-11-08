package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        val expectedBook = em.find(Comment.class, UUID.fromString("60ebe253-6b1f-410f-b159-8f51a6026ec3"));
        val actualBook = commentRepository.findById(UUID.fromString("60ebe253-6b1f-410f-b159-8f51a6026ec3"));
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsListByBookId() {
        var actualComments = commentRepository
                .findAllByBookId(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"));
        var expectedComments = getDbCommentsByBookId();

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(UUID.fromString("1bc5a43c-b3aa-4ce1-92c8-6440bb908112"),
                "CommentText_X", getDbBook());
        var returnedComment = commentRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(UUID.fromString("60ebe253-6b1f-410f-b159-8f51a6026ec3"),
                "CommentText_X", getDbBook());

        assertThat(commentRepository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedBook = commentRepository.save(expectedComment);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertThat(commentRepository.findById(UUID.fromString("cbee18e7-f448-479d-b8d7-2048c087b5a0"))).isPresent();
        commentRepository.deleteById(UUID.fromString("cbee18e7-f448-479d-b8d7-2048c087b5a0"));
        assertThat(em.find(Comment.class, UUID.fromString("cbee18e7-f448-479d-b8d7-2048c087b5a0"))).isNull();
    }

    private List<Comment> getDbCommentsByBookId() {
        return  List.of(em.find(Comment.class, UUID.fromString("60ebe253-6b1f-410f-b159-8f51a6026ec3")),
                em.find(Comment.class, UUID.fromString("cbee18e7-f448-479d-b8d7-2048c087b5a0"))
        );
    }

    private Book getDbBook() {
        return em.find(Book.class, UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"));
    }

}
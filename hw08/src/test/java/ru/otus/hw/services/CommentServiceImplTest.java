package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис работы с комментариями должен ")
@DataMongoTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({CommentServiceImpl.class})
public class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    private static Book getBookData() {
        return new Book("1", "BookTitle_1", getAuthorData(), getGenreData());
    }

    private static Author getAuthorData() {
        return new Author("1", "Author_1");
    }

    private static List<Genre> getGenreData() {
        return List.of(new Genre("1", "Genre_1"), new Genre("2", "Genre_2"));
    }

    private static Comment getCommentData() {
        return new Comment("1", "Comment_1", getBookData());
    }

    private static Comment getNewCommentData() {
        return new Comment("4", "New_Comment_1_Book_1", getBookData());
    }

    @Test
    @DisplayName(" вернуть корректный комментарий по id")
    public void shouldReturnCorrectCommentById() {
        Optional<Comment> actualComment = commentService.findById("1");
        var expectedComment = getCommentData();

        assertThat(actualComment).isPresent();
        assertThat(actualComment.get().getId()).isEqualTo(expectedComment.getId());
        assertThat(actualComment.get().getText()).isEqualTo(expectedComment.getText());
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualComments = commentService.findAllByBookId("1");

        assertThat(actualComments).isNotEmpty()
                .hasSize(2)
                .hasOnlyElementsOfType(Comment.class);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = getNewCommentData();
        var actualComment = commentService.create(expectedComment.getText(), expectedComment.getBook().getId());
        assertThat(actualComment).isNotNull()
                .matches(comment -> !comment.getId().isBlank());
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
        assertThat(actualComment.getBook().getId()).isEqualTo(expectedComment.getBook().getId());
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = getCommentData();
        expectedComment.setText("New_Comment_1_Book_1");

        assertThat(commentService.findById(expectedComment.getId()))
                .isPresent();

        assertThat(commentService.findById(expectedComment.getId()).get().getText()).isNotEqualTo(
                expectedComment.getText());

        var actualComment = commentService.update("1", expectedComment.getText());

        assertThat(commentService.findById(actualComment.getId()))
                .isPresent();
        assertThat(actualComment.getId()).isEqualTo(expectedComment.getId());
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(commentService.findById("1")).isPresent();
        commentService.deleteById("1");

        var actualComment = commentService.findById("1");
        assertThat(actualComment).isEmpty();
    }

}
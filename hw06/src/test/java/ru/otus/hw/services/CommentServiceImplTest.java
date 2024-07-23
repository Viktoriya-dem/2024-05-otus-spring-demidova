package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис работы с комментариями должен ")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({CommentServiceImpl.class, JpaBookRepository.class, JpaCommentRepository.class})
public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    @DisplayName(" вернуть корректный комментарий по id")
    public void shouldReturnCorrectCommentById() {
        Optional<Comment> actualComment = commentService.findById(1L);
        var expectedComment = getCommentData();

        assertThat(actualComment).isPresent();
        compareComment(actualComment.get(), expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualComments = commentService.findAllByBookId(1L);

        assertThat(actualComments).isNotEmpty()
                .hasSize(2)
                .hasOnlyElementsOfType(Comment.class);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = getNewCommentData();
        var actualComment = commentService.insert(expectedComment.getText(), expectedComment.getBook().getId());
        assertThat(actualComment).isNotNull()
                .matches(comment -> comment.getId() > 0);

        compareComment(actualComment, expectedComment);
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

        var actualComment = commentService.update(1L, expectedComment.getText(), expectedComment.getBook().getId());
        assertThat(actualComment).isNotNull()
                .matches(comment -> comment.getId() > 0);

        assertThat(commentService.findById(actualComment.getId()))
                .isPresent();

        compareComment(actualComment, expectedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(commentService.findById(1L)).isPresent();
        commentService.deleteById(1L);

        var actualComment = commentService.findById(1L);
        assertThat(actualComment).isEmpty();
    }

    public void compareComment(Comment actualComment, Comment expectedComment) {
        assertThat(actualComment.getId()).isEqualTo(expectedComment.getId());
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
    }

    private static Book getBookData() {
        return new Book(1L, "BookTitle_1", getAuthorData(), getGenreData());
    }

    private static Author getAuthorData() {
        return new Author(1L, "Author_1");
    }

    private static List<Genre> getGenreData() {
        return List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"));
    }

    private static Comment getCommentData() {
        return new Comment(1L, "Comment_1_Book_1", getBookData());
    }

    private static Comment getNewCommentData() {
        return new Comment(4L, "New_Comment_1_Book_1", getBookData());
    }

}
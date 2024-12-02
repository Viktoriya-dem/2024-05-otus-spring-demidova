package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mappers.*;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис работы с комментариями должен ")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({CommentServiceImpl.class, BookMapperImpl.class, CommentMapperImpl.class, AuthorMapperImpl.class,
GenreMapperImpl.class})
public class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    BookMapper bookMapper;

    CommentMapper commentMapper;
    @BeforeEach
    void setup() {
        bookMapper = Mappers.getMapper(BookMapper.class);
        commentMapper = Mappers.getMapper(CommentMapper.class);
    }

    private static Book getBookData() {
        return new Book(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"), "BookTitle_1",
                getAuthorData(), getGenreData());
    }

    private static Author getAuthorData() {
        return new Author(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "Author_1");
    }

    private static List<Genre> getGenreData() {
        return List.of(new Genre(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"), "Genre_1"),
                new Genre(UUID.fromString("980fab3b-338d-45e7-83b6-29b98d1c4b02"), "Genre_2"));
    }

    private static Comment getCommentData() {
        return new Comment(UUID.fromString("60ebe253-6b1f-410f-b159-8f51a6026ec3"), "Comment_1_Book_1", getBookData());
    }

    private static CommentDto getNewCommentData() {
        return new CommentDto(UUID.fromString("5edceec3-390a-491f-ab83-ccbaeeb3da1c"), "New_Comment_1_Book_1",
                UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"));
    }


    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualComments = commentService.findAllByBookId(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"));

        assertThat(actualComments).isNotEmpty()
                .hasSize(2)
                .hasOnlyElementsOfType(CommentDto.class);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = getNewCommentData();
        var actualComment = commentService.create(expectedComment.getId(),
                expectedComment.getText(), expectedComment.getBookId());
        assertThat(actualComment).isNotNull()
                .matches(comment -> comment.getId() != null);
        assertThat(actualComment.getId()).isEqualTo(expectedComment.getId());
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
        assertThat(actualComment.getBookId()).isEqualTo(expectedComment.getBookId());
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = getCommentData();
        expectedComment.setText("New_Comment_1_Book_1");

        assertThat(commentService.findById(expectedComment.getId()))
                .isNotNull();

        assertThat(commentService.findById(expectedComment.getId()).getText()).isNotEqualTo(
                expectedComment.getText());

        var actualComment = commentService.update(UUID.fromString("60ebe253-6b1f-410f-b159-8f51a6026ec3"),
                expectedComment.getText());

        assertThat(commentService.findById(actualComment.getId()))
                .isNotNull();
        assertThat(actualComment.getId()).isEqualTo(expectedComment.getId());
        assertThat(actualComment.getText()).isEqualTo(expectedComment.getText());
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(commentService.findById(UUID.fromString("60ebe253-6b1f-410f-b159-8f51a6026ec3"))).isNotNull();
        commentService.deleteById(UUID.fromString("60ebe253-6b1f-410f-b159-8f51a6026ec3"));

        assertThatThrownBy(() -> commentService.findById(UUID.fromString("60ebe253-6b1f-410f-b159-8f51a6026ec3")))
                .isInstanceOf(Exception.class)
                .hasMessage("Comment not found");
    }

}
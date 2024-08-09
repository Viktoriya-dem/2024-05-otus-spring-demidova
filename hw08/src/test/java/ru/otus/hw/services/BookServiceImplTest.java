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
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис работы с книгами должен ")
@DataMongoTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({BookServiceImpl.class})
public class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    private static Book getBookData() {
        return new Book("1", "BookTitle_1", getAuthorData(), getGenreData());
    }

    private static Book getNewBookData() {
        return new Book("4", "NewBookTitle_1", getAuthorData(), getGenreData());
    }

    private static Author getAuthorData() {
        return new Author("1", "Author_1");
    }

    private static List<Genre> getGenreData() {
        return List.of(new Genre("1", "Genre_1"), new Genre("2", "Genre_2"));
    }

    @Test
    @DisplayName(" вернуть корректную книгу по id")
    public void shouldReturnCorrectBookById() {
        Optional<Book> actualBook = bookService.findById("1");
        var expectedBook = getBookData();

        assertThat(actualBook).isPresent();

        assertThat(actualBook.get()).isNotNull()
                .matches(book -> !book.getId().isBlank())
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookService.findAll();

        assertThat(actualBooks).isNotEmpty()
                .hasSize(3)
                .hasOnlyElementsOfType(Book.class);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = getNewBookData();
        var actualBook = bookService.create(expectedBook.getTitle(), expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
        assertThat(actualBook).isNotNull()
                .matches(book -> !book.getId().isBlank())
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = getBookData();
        expectedBook.setTitle("Change_Title_1");

        assertThat(bookService.findById(expectedBook.getId()))
                .isPresent();

        assertThat(bookService.findById(expectedBook.getId()).get().getTitle()).isNotEqualTo(expectedBook.getTitle());

        var actualBook = bookService.update("1", expectedBook.getTitle(), expectedBook.getAuthor().getId(),
                expectedBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
        assertThat(actualBook).isNotNull()
                .matches(book -> !book.getId().isBlank())
                .usingRecursiveComparison().isEqualTo(expectedBook);

        assertThat(bookService.findById(actualBook.getId()))
                .isPresent();
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(bookService.findById("3")).isPresent();
        bookService.deleteById("3");

        var actualBook = bookService.findById("3");
        assertThat(actualBook).isEmpty();
    }

}
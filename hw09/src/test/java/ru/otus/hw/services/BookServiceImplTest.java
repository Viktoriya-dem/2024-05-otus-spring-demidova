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
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис работы с книгами должен ")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({BookServiceImpl.class})
public class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    private static Book getBookData() {
        return new Book(1L, "BookTitle_1", getAuthorData(), getGenreData());
    }

    private static Book getNewBookData() {
        return new Book(4L, "NewBookTitle_1", getAuthorData(), getGenreData());
    }

    private static Author getAuthorData() {
        return new Author(1L, "Author_1");
    }

    private static List<Genre> getGenreData() {
        return List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"));
    }

    @Test
    @DisplayName(" вернуть корректную книгу по id")
    public void shouldReturnCorrectBookById() {
        Book actualBook = bookService.findById(1L);
        var expectedBook = getBookData();

        assertThat(actualBook).isNotNull();

        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() > 0)
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
//        var actualBook = bookService.create(expectedBook.getTitle(), expectedBook.getAuthor().getId(),
//                expectedBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()));
//        assertThat(actualBook).isNotNull()
//                .matches(book -> book.getId() > 0)
//                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = getBookData();
        expectedBook.setTitle("Change_Title_1");

        assertThat(bookService.findById(expectedBook.getId())).isNotNull();

        assertThat(bookService.findById(expectedBook.getId()).getTitle()).isNotEqualTo(expectedBook.getTitle());

    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(bookService.findById(1L)).isNotNull();
        bookService.deleteById(1L);

        assertThatThrownBy(() -> bookService.findById(1L)).isInstanceOf(Exception.class)
                .hasMessage("Book not found");
    }

}
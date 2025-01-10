package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoFullInfo;
import ru.otus.hw.mappers.BookMapperImpl;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис работы с книгами должен ")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({BookServiceImpl.class, BookMapperImpl.class})
public class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    private static BookDto getBookData() {
        return new BookDto(1L, "BookTitle_1", 1L, Set.of(1L, 2L));
    }

    private static BookDto getNewBookData() {
        return new BookDto(4L, "NewBookTitle_1", 2L, Set.of(2L, 3L));
    }

    @Test
    @DisplayName(" вернуть корректную книгу по id")
    public void shouldReturnCorrectBookById() {
        BookDto actualBook = bookService.findById(1L);
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
                .hasOnlyElementsOfType(BookDtoFullInfo.class);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = getNewBookData();
        var actualBook = bookService.create(expectedBook);
        expectedBook.setId(4L);

        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().isEqualTo(expectedBook);
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
package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.GenreMapperImpl;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Сервис работы с книгами должен ")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({BookServiceImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
public class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    public BookDto getBookDto1(){
        return new BookDto(1L, "BookTitle_1", new AuthorDto(1L, "Author_1"),
                Set.of(new GenreDto(1L, "Genre_1"), new GenreDto(2L, "Genre_2")));
    }

    private static BookDto getNewBookData() {
        return new BookDto(4L, "NewBookTitle_1", new AuthorDto(1L, "Author_1"),
                Set.of(new GenreDto(1L, "Genre_1")));
    }

    @Test
    @DisplayName(" вернуть корректную книгу по id")
    public void shouldReturnCorrectBookById() {
        BookDto actualBook = bookService.findById(1L);
        var expectedBook = getBookDto1();

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
                .hasOnlyElementsOfType(BookDto.class);
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
        var expectedBook = getBookDto1();
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
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
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.GenreMapperImpl;

import java.util.Set;
import java.util.UUID;

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

    public BookDto getBookDto1() {
        return new BookDto(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"), "BookTitle_1",
                new AuthorDto(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "Author_1"),
                Set.of(new GenreDto(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"), "Genre_1"),
                        new GenreDto(UUID.fromString("980fab3b-338d-45e7-83b6-29b98d1c4b02"), "Genre_2")));
    }

    private static BookDto getNewBookData() {
        return new BookDto(UUID.fromString("a36adce6-4b88-4ee8-a974-b3d4bd5fb3d8"), "NewBookTitle_1",
                new AuthorDto(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "Author_1"),
                Set.of(new GenreDto(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"), "Genre_1")));
    }

    @Test
    @DisplayName(" вернуть корректную книгу по id")
    public void shouldReturnCorrectBookById() {
        BookDto actualBook = bookService.findById(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"));
        var expectedBook = getBookDto1();

        assertThat(actualBook).isNotNull();

        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() != null)
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
        BookCreateDto bookCreateDto = new BookCreateDto(expectedBook.getId(),
                expectedBook.getTitle(), expectedBook.getAuthor(),expectedBook.getGenres());

        var actualBook = bookService.create(bookCreateDto);

        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() != null)
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
        assertThat(bookService.findById(UUID.fromString("5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1"))).isNotNull();
        bookService.deleteById(UUID.fromString("5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1"));

        assertThatThrownBy(() -> bookService.findById(UUID.fromString("5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1")))
                .isInstanceOf(Exception.class).hasMessage("Book not found");
    }

}
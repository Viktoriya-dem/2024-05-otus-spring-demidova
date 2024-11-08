package ru.otus.hw.repositories;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        val expectedBook = em.find(Book.class, UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"));
        val actualBook = bookRepository.findById(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"));
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();
        var expectedBooks = getDbBooks();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(UUID.fromString("f7d5d349-52d9-4977-b13f-527ffcab6753"), "BookTitle_X",
                getDbAuthor(),
                List.of(getDbGenre()));
        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(UUID.fromString("5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1"), "BookTitle_X",
                getDbAuthor(),
                List.of(getDbGenre()));

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(bookRepository.findById(UUID.fromString("5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1"))).isPresent();
        bookRepository.deleteById(UUID.fromString("5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1"));
        assertThat(em.find(Book.class, UUID.fromString("5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1"))).isNull();
    }

    private List<Book> getDbBooks() {
        return  List.of(em.find(Book.class, UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848")),
                em.find(Book.class, UUID.fromString("f7b16ec4-3e96-4693-b761-db978faf0087")),
                em.find(Book.class, UUID.fromString("5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1"))
        );
    }

    private Author getDbAuthor() {
        return em.find(Author.class, UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"));
    }

    private Genre getDbGenre() {
        return em.find(Genre.class,
                UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"));
    }
}
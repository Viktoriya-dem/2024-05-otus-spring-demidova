package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import lombok.val;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.UUID;

@ChangeLog
public class DatabaseChangelog {

    private Author author1;

    private Author author2;

    private Author author3;

    private Genre genre1;

    private Genre genre2;

    private Genre genre3;

    private Book book1;

    private Book book2;

    @ChangeSet(order = "001", id = "dropDb", author = "v.demidova", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "v.demidova", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        author1 = repository.save(new Author(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"),
                "Author_1")).block();
        author2 = repository.save(new Author(UUID.fromString("30df0652-0b5d-40af-86d9-cd336b836648"),
                "Author_2")).block();
        author3 = repository.save(new Author(UUID.fromString("57daec39-1f36-4248-abaf-9e12bb9e77f5"),
                "Author_3")).block();
    }

    @ChangeSet(order = "003", id = "initGenres", author = "v.demidova", runAlways = true)
    public void initGenres(GenreRepository repository) {
        genre1 = repository.save(new Genre(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"),
                "Genre_1")).block();
        genre2 = repository.save(new Genre(UUID.fromString("980fab3b-338d-45e7-83b6-29b98d1c4b02"),
                "Genre_2")).block();
        genre3 = repository.save(new Genre(UUID.fromString("66f51d2d-e6b7-4602-8b21-31439ea1f721"),
                "Genre_3")).block();
    }

    @ChangeSet(order = "004", id = "Book", author = "v.demidova", runAlways = true)
    public void initBooks(BookRepository repository) {
        book1 = new Book(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                "BookTitle_1", author1, genre1, genre2);
        repository.save(book1).block();
        book2 = new Book(UUID.fromString("f7b16ec4-3e96-4693-b761-db978faf0087"),
                "BookTitle_2", author2, genre2);
        repository.save(book2).block();
        val book3 = new Book(UUID.fromString("5cf0a359-82e1-4ddf-9c5c-d54bb50fefe1"),
                "BookTitle_3", author3, genre2, genre3);
        repository.save(book3).block();
    }

    @ChangeSet(order = "005", id = "Comment", author = "v.demidova", runAlways = true)
    public void initComments(CommentRepository repository) {
        val comment1 = new Comment(UUID.fromString("60ebe253-6b1f-410f-b159-8f51a6026ec3"),
                "Comment_1", book1);
        repository.save(comment1).block();
        val comment2 = new Comment(UUID.fromString("cbee18e7-f448-479d-b8d7-2048c087b5a0"),
                "Comment_2", book1);
        repository.save(comment2).block();
        val comment3 = new Comment(UUID.fromString("45da304-56a5-4ff9-a982-1713a42a3c56e"),
                "Comment_3", book2);
        repository.save(comment3).block();
    }
}

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
        author1 = repository.save(new Author("1", "Author_1"));
        author2 = repository.save(new Author("2", "Author_2"));
        author3 = repository.save(new Author("3", "Author_3"));
    }

    @ChangeSet(order = "003", id = "initGenres", author = "v.demidova", runAlways = true)
    public void initGenres(GenreRepository repository) {
        genre1 = repository.save(new Genre("1", "Genre_1"));
        genre2 = repository.save(new Genre("2", "Genre_2"));
        genre3 = repository.save(new Genre("3", "Genre_3"));
    }

    @ChangeSet(order = "004", id = "Book", author = "v.demidova", runAlways = true)
    public void initBooks(BookRepository repository) {
        book1 = new Book("1", "BookTitle_1", author1, genre1, genre2);
        repository.save(book1);
        book2 = new Book("2", "BookTitle_2", author2, genre2);
        repository.save(book2);
        val book3 = new Book("3", "BookTitle_3", author3, genre2, genre3);
        repository.save(book3);
    }

    @ChangeSet(order = "005", id = "Comment", author = "v.demidova", runAlways = true)
    public void initComments(CommentRepository repository) {
        val comment1 = new Comment("1", "Comment_1", book1);
        repository.save(comment1);
        val comment2 = new Comment("2", "Comment_2", book1);
        repository.save(comment2);
        val comment3 = new Comment("3", "Comment_3", book2);
        repository.save(comment3);
    }
}

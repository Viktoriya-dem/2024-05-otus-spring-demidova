package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "books")
public class Book {
    @Id
    private UUID id;

    private String title;

    private Author author;

    private List<Genre> genres;

    public Book(UUID id, String title, Author author, Genre... genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = Arrays.asList(genres);
    }
}

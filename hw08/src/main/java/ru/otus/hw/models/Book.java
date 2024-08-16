package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String title;

    private Author author;

    private List<Genre> genres;

    public Book(String id, String title, Author author, Genre... genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = Arrays.asList(genres);
    }
}

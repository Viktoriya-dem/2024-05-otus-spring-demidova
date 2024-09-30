package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.List;

public interface BookService {
    Book findById(long id);

    List<Book> findAll();

    Book save(Book book);

    Book insert(BookDto bookDto);

    Book update(BookDto bookDto);

    void deleteById(long id);
}

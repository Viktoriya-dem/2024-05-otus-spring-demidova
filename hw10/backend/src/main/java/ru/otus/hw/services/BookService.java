package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(BookDto bookDto);

    BookDto update(BookDto bookDto);

    void deleteById(long id);
}

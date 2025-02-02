package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoFullInfo;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDtoFullInfo> findAll();

    BookDto create(BookDto bookDto);

    BookDto update(BookDto bookDto);

    void deleteById(long id);
}

package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> getAllBooks() {

        return bookService.findAll();
    }

    @GetMapping("/api/books/{id}")
    public BookDto getBookById(@PathVariable UUID id) {

        return bookService.findById(id);
    }

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody @Valid BookCreateDto bookDto) {

        return bookService.create(bookDto);
    }

    @PatchMapping("/api/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto updateBook(@PathVariable UUID id, @RequestBody @Valid BookUpdateDto bookDto) {

        bookDto.setId(id);
        return bookService.update(bookDto);
    }

    @DeleteMapping("/api/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable UUID id) {
        bookService.deleteById(id);
    }

}

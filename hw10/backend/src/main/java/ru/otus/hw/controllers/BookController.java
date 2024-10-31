package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public List<BookDto> getAllBooks() {

        return bookService.findAll();
    }

    @GetMapping("/books/{id}")
    public BookDto getAllBooks(@PathVariable long id) {

        return bookService.findById(id);
    }

    @PostMapping("/books")
    public ResponseEntity<String> createBook(@RequestBody @Valid BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
        }

        bookService.create(bookDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/books")
    public ResponseEntity<String> updateBook(@RequestBody @Valid BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
        }

        bookService.update(bookDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }

}

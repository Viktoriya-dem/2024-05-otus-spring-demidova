package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoFullInfo;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/books/all";
    }

    @GetMapping("/books/all")
    public String getAllBooks(Model model) {
        List<BookDtoFullInfo> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books/book-list";
    }

    @GetMapping("books/create")
    public String createBook(Model model) {
        BookDto book = new BookDto();
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "books/edit";
    }

    @PostMapping("/books/create")
    public String createBook(@Valid @ModelAttribute("book") BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "books/edit";
        }

        bookService.create(bookDto);

        return "redirect:/books/all";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable long id, Model model) {
        BookDto book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());

        return "/books/edit";
    }

    @PostMapping("/books/edit")
    public String saveBook(@Valid @ModelAttribute("book") BookDto bookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", genreService.findAll());
            model.addAttribute("authors", authorService.findAll());
            return "books/edit";
        }

        bookService.update(bookDto);
        return "redirect:/books/all";
    }

    @PostMapping("/books/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);

        return "redirect:/books/all";
    }
}

package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);

        return "books/book-list";
    }

    @GetMapping("/books/{id}")
    public String getBookById(@PathVariable long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);

        return "books/book";
    }

    @GetMapping("/books/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book", book);

        return "/books/edit";
    }

    @PostMapping("/books/edit")
    public String saveBook(Book book) {
        bookService.save(book);

        return "redirect:/books";
    }

    @DeleteMapping("/books/{id}")
    public String deleteById(@PathVariable("id") long id) {
        bookService.deleteById(id);

        return "redirect:/books";
    }

    @RequestMapping(value = "/genreDropDownList", method = RequestMethod.GET)
    public String genreList(Model model) {
        List<String> genres = new ArrayList<String>();
        options.add("option 1");
        options.add("option 2");
        options.add("option 3");
        model.addAttribute("genres", options);
        return "dropDownList/genreDropDownList.html";

    }

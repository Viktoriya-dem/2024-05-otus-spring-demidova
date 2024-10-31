package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final BookService bookService;

    private final CommentService commentService;


    @GetMapping("/comments/book/{id}")
    public List<CommentDto> getComments(@PathVariable long id) {
        BookDto bookDto = bookService.findById(id);

        return commentService.findAllByBookId(id);
    }

    @PostMapping("/comments")
    public ResponseEntity<String> createComment(@RequestBody @Valid CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
        }

        commentService.create(commentDto.getText(), commentDto.getBookId());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/comments")
    public ResponseEntity<String> updateComment(@RequestBody @Valid CommentDto commentDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().toString());
        }

        commentService.update(commentDto.getId(), commentDto.getText());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long id) {
        commentService.deleteById(id);
    }

}

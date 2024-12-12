package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/comments/book/{id}")
    public List<CommentDto> getComments(@PathVariable UUID id) {

        return commentService.findAllByBookId(id);
    }

    @PostMapping("/api/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody @Valid CommentDto commentDto) {
        return commentService.create(commentDto.getId(), commentDto.getText(), commentDto.getBookId());
    }

    @PatchMapping("/api/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@RequestBody @Valid CommentDto commentDto) {
        return commentService.update(commentDto.getId(), commentDto.getText());
    }

    @DeleteMapping("/api/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable UUID id) {
        commentService.deleteById(id);
    }

}

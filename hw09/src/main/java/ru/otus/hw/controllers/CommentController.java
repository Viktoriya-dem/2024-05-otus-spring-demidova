package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

//    @GetMapping("/comments/{id}")
//    public String findAllBookComments(@PathVariable("id") long bookId) {
//        return commentService.findAllByBookId(bookId).stream()
//                .map(commentConverter::commentToString)
//                .collect(Collectors.joining("," + System.lineSeparator()));
//    }
//
//    @ShellMethod(value = "Find comment by id", key = "cbid")
//    public String findCommentById(long id) {
//        return commentService.findById(id)
//                .map(commentConverter::commentToString)
//                .orElse("Comment with id %d not found".formatted(id));
//    }
//
//    // cins newComment 1
//    @ShellMethod(value = "Create comment", key = "cins")
//    public String createComment(String text, long bookId) {
//        var savedComment = commentService.create(text, bookId);
//        return commentConverter.commentToString(savedComment);
//    }
//
//    // bupd 4 editedComment 2
//    @ShellMethod(value = "Update comment", key = "cupd")
//    public String updateComment(long id, String text) {
//        var savedComment = commentService.update(id, text);
//        return commentConverter.commentToString(savedComment);
//    }
//
//    // cdel 4
//    @ShellMethod(value = "Delete comment by id", key = "cdel")
//    public void deleteComment(long id) {
//        commentService.deleteById(id);
//    }
}

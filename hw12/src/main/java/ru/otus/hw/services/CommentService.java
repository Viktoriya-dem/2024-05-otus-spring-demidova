package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {
    Comment findById(long id);

    List<Comment> findAllByBookId(long bookId);

    Comment create(String text, long bookId);

    Comment update(long id, String title);

    void deleteById(long id);
}

package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {
    Comment findById(long id);

    List<CommentDto> findAllByBookId(long bookId);

    CommentDto create(String text, long bookId);

    CommentDto update(long id, String title);

    void deleteById(long id);
}

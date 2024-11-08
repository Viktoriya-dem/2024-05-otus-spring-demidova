package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    Comment findById(UUID id);

    List<CommentDto> findAllByBookId(UUID bookId);

    CommentDto create(UUID id, String text, UUID bookId);

    CommentDto update(UUID id, String title);

    void deleteById(UUID id);
}

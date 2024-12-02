package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public Comment findById(UUID id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByBookId(UUID bookId) {
        return commentMapper.toDto(commentRepository.findAllByBookId(bookId));
    }

    @Transactional
    public CommentDto create(UUID id, String text, UUID bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto update(UUID id, String text) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id %d not found".formatted(id)));
        comment.setText(text);

        return commentMapper.toDto(commentRepository.save(comment));

    }

    @Transactional
    public void deleteById(UUID id) {
        commentRepository.deleteById(id);
    }
}

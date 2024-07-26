package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByBookId(long bookId) {
        return commentRepository.findAllByBookId(bookId);
    }

    @Transactional
    public Comment insert(String text, long bookId) {
        return save(0, text, bookId);
    }

    @Transactional
    public Comment update(long id, String text) {
        var comment = commentRepository.findById(id);

        if (comment.isPresent()) {
            comment.get().setText(text);

            return commentRepository.save(comment.get());
        } else {
            throw new EntityNotFoundException("Comment with id %d not found".formatted(id));
        }

    }

    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);

        return commentRepository.save(comment);
    }
}

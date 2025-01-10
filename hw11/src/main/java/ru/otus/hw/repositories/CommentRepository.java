package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Comment;

import java.util.UUID;

public interface CommentRepository extends ReactiveMongoRepository<Comment, UUID> {

    Flux<Comment> findAllByBookId(UUID bookId);

    void deleteAllByBookId(UUID bookId);
}

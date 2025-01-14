package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.models.Author;

import java.util.UUID;

public interface AuthorRepository extends ReactiveMongoRepository<Author, UUID> {
}

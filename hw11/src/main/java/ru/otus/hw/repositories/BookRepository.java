package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.models.Book;

import java.util.UUID;


public interface BookRepository extends ReactiveMongoRepository<Book, UUID> {

}

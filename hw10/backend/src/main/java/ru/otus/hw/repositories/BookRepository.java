package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Override
    @EntityGraph("book-all-graph")
    Optional<Book> findById(UUID id);

    @Override
    @EntityGraph("book-author-graph")
    List<Book> findAll();

}

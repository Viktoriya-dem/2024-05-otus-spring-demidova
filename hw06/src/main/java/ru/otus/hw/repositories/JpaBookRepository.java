package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-graph");
        Map<String, Object> params = Map.of("jakarta.persistence.fetchgraph", entityGraph);

        Book book = em.find(Book.class, id, params);

        if (book != null) {
            book.getGenres().size();
        }

        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b ", Book.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        List<Book> books = query.getResultList();

        if (!books.isEmpty()) {
            books.get(0).getGenres().size();
        }

        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        Book book = em.find(Book.class, id);

        if (book != null) {
            em.remove(book);
        }
    }

}

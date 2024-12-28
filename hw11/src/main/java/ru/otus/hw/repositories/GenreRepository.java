package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Genre;

import java.util.Set;
import java.util.UUID;

public interface GenreRepository extends ReactiveMongoRepository<Genre, UUID> {

    Flux<Genre> findAllByIdIn(Set<UUID> ids);
}

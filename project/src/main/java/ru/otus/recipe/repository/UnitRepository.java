package ru.otus.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.recipe.model.Unit;

import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {
}

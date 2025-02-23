package ru.otus.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.recipe.model.Category;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}

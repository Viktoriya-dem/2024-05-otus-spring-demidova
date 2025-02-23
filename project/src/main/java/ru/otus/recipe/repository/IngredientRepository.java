package ru.otus.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.recipe.model.Ingredient;

import java.util.List;
import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

    List<Ingredient> findAllByIdIn(List<UUID> ids);

}

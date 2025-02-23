package ru.otus.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.recipe.model.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}

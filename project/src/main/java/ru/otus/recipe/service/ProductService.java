package ru.otus.recipe.service;

import ru.otus.recipe.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> findAll();

}

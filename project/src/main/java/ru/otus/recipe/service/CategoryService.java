package ru.otus.recipe.service;

import ru.otus.recipe.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> findAll();

}

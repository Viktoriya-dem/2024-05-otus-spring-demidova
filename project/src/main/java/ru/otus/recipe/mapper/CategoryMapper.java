package ru.otus.recipe.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.otus.recipe.dto.CategoryDto;
import ru.otus.recipe.model.Category;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
@Component
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    List<CategoryDto> toDto(List<Category> products);

    Category toEntity(CategoryDto categoryDto);
}
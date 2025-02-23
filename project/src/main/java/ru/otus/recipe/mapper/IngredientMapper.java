package ru.otus.recipe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.otus.recipe.dto.IngredientDto;
import ru.otus.recipe.model.Ingredient;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {ProductMapper.class, UnitMapper.class})
@Component
public interface IngredientMapper {

    @Mapping(target = "productDto", source = "product")
    @Mapping(target = "unitDto", source = "unit")
    IngredientDto toDto(Ingredient ingredient);

    List<IngredientDto> toDto(List<Ingredient> ingredients);

    @Mapping(target = "product", source = "productDto")
    @Mapping(target = "unit", source = "unitDto")
    Ingredient toEntity(IngredientDto ingredientDto);

}

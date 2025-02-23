package ru.otus.recipe.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    @NotNull
    private UUID id;

    @NotNull
    private ProductDto productDto;

    @NotNull
    private Float quantity;

    @NotNull
    private UnitDto unitDto;

}

package ru.otus.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

    @NotNull
    private UUID id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String cooking;

    private AttachmentDto attachmentDto;

    private UserDto userDto;

    @NotNull
    private CategoryDto categoryDto;

    @NotNull
    private List<IngredientDto> ingredients;

    private LocalDate createdDate;

}

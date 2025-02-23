package ru.otus.recipe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.otus.recipe.dto.RecipeDto;
import ru.otus.recipe.model.Attachment;
import ru.otus.recipe.model.Category;
import ru.otus.recipe.model.Recipe;
import ru.otus.recipe.model.User;
import ru.otus.recipe.service.AttachmentService;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {AttachmentService.class, IngredientMapper.class,
        ProductMapper.class, UnitMapper.class, AttachmentMapper.class})
@Component
public interface RecipeMapper {

    @Mapping(target = "userDto", source = "user")
    @Mapping(target = "categoryDto", source = "category")
    @Mapping(target = "attachmentDto", source = "attachment")
    @Mapping(target = "ingredients", source = "ingredients")
    RecipeDto toDto(Recipe recipe);

    @Mapping(target = "attachment", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    default Recipe toEntity(RecipeDto recipeDto, Attachment attachment,
                            User user,
                            Category category) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeDto.getId());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setName(recipeDto.getName());
        recipe.setCooking(recipeDto.getCooking());
        recipe.setAttachment(attachment);
        recipe.setUser(user);
        recipe.setCategory(category);

        return recipe;
    }

    List<RecipeDto> toDto(List<Recipe> recipes);

}

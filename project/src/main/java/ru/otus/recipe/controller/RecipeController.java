package ru.otus.recipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.recipe.dto.RecipeDto;
import ru.otus.recipe.dto.UserDto;
import ru.otus.recipe.service.RecipeService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/api/recipes")
    public List<RecipeDto> getAllRecipes() {

        return recipeService.findAll();
    }

    @GetMapping("/api/recipes/user")
    public List<RecipeDto> getAllUserRecipes(Principal principal) {

        return recipeService.findAllByUserId(principal);
    }

    @GetMapping("/api/recipes/{id}")
    public RecipeDto getRecipeById(@PathVariable UUID id) {

        return recipeService.findById(id);
    }

    @PostMapping("/api/recipes")
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto createRecipe(Principal principal, @RequestPart("recipe") @Valid RecipeDto recipeDto,
                                  @RequestPart("file") MultipartFile uploadedFile) {
        UserDto userDto = new UserDto();
        userDto.setUsername(principal.getName());
        recipeDto.setUserDto(userDto);
        return recipeService.create(recipeDto, uploadedFile);
    }

    @PatchMapping("/api/recipes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeDto updateRecipe(Principal principal, @PathVariable UUID id,
                                  @RequestPart("recipe") @Valid RecipeDto recipeDto,
                                  @RequestPart("file") MultipartFile uploadedFile) {
        recipeDto.setId(id);
        UserDto userDto = new UserDto();
        userDto.setUsername(principal.getName());
        recipeDto.setUserDto(userDto);
        return recipeService.update(recipeDto, uploadedFile);
    }

    @DeleteMapping("/api/recipes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(Principal principal, @PathVariable UUID id) {
        recipeService.deleteById(principal, id);
    }

}

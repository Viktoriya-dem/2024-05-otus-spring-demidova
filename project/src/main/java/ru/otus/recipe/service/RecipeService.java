package ru.otus.recipe.service;

import org.springframework.web.multipart.MultipartFile;
import ru.otus.recipe.dto.RecipeDto;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface RecipeService {

    RecipeDto findById(UUID id);

    List<RecipeDto> findAll();

    List<RecipeDto> findAllByUserId(Principal principal);

    RecipeDto create(RecipeDto recipeDto, MultipartFile multipartFile);

    RecipeDto update(RecipeDto recipeDto, MultipartFile multipartFile);

    void deleteById(Principal principal, UUID id);

}

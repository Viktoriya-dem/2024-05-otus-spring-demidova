package ru.otus.recipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.recipe.dto.IngredientDto;
import ru.otus.recipe.dto.RecipeDto;
import ru.otus.recipe.exception.NotFoundException;
import ru.otus.recipe.exception.NotUserRecipeException;
import ru.otus.recipe.mapper.IngredientMapper;
import ru.otus.recipe.mapper.ProductMapper;
import ru.otus.recipe.mapper.RecipeMapper;
import ru.otus.recipe.mapper.UnitMapper;
import ru.otus.recipe.model.Category;
import ru.otus.recipe.model.Ingredient;
import ru.otus.recipe.model.Product;
import ru.otus.recipe.model.Recipe;
import ru.otus.recipe.model.Unit;
import ru.otus.recipe.model.User;
import ru.otus.recipe.repository.CategoryRepository;
import ru.otus.recipe.repository.IngredientRepository;
import ru.otus.recipe.repository.ProductRepository;
import ru.otus.recipe.repository.RecipeRepository;
import ru.otus.recipe.repository.UnitRepository;
import ru.otus.recipe.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class RecipeServiceImpl implements RecipeService {

    private final AttachmentServiceImpl attachmentService;

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final IngredientRepository ingredientRepository;

    private final UserRepository userRepository;

    private final UnitRepository unitRepository;

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    private final UnitMapper unitMapper;

    private final ProductMapper productMapper;

    private final IngredientMapper ingredientMapper;

    @Override
    @Transactional(readOnly = true)
    public RecipeDto findById(UUID id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found"));
        return recipeMapper.toDto(recipe);
    }

    @Override
    @Transactional
    public List<RecipeDto> findAll() {
        List<Recipe> recipes = recipeRepository.findAll();

        return recipeMapper.toDto(recipes);
    }

    @Override
    @Transactional
    public List<RecipeDto> findAllByUserId(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new NotFoundException("User with name %s not found"
                        .formatted(principal.getName())));

        List<Recipe> recipes = recipeRepository.findAllByUserId(user.getId());

        return recipeMapper.toDto(recipes);
    }

    @Override
    @Transactional
    public RecipeDto create(RecipeDto recipeDto, MultipartFile multipartFile) {

        Recipe recipe = recipeMapper.toEntity(recipeDto, attachmentService.addAttachment(multipartFile,
                        recipeDto.getAttachmentDto().getId()),
                userRepository.findByUsername(recipeDto.getUserDto().getUsername())
                        .orElseThrow(() -> new NotFoundException("User with name %s not found"
                                .formatted(recipeDto.getUserDto().getId()))),
                getCategory(recipeDto.getCategoryDto().getId()));
        recipe.setCreatedDate(LocalDate.now());

        recipe = recipeRepository.save(recipe);
        List<Ingredient> ingredients = createIngredients(recipeDto.getIngredients());

        for (Ingredient ingredient : ingredients) {
            ingredient.setRecipe(recipe);
        }

        recipe.setIngredients(ingredients);
        recipeRepository.save(recipe);

        return recipeMapper.toDto(recipeRepository.findById(recipe.getId()).get());
    }

    @Override
    @Transactional
    public RecipeDto update(RecipeDto recipeDto, MultipartFile multipartFile) {
        User user = userRepository.findByUsername(recipeDto.getUserDto().getUsername()).orElseThrow(()
                -> new NotFoundException("User with name %s not found".formatted(recipeDto.getUserDto().getUsername())));
        Recipe recipe = recipeRepository.findById(recipeDto.getId())
                .orElseThrow(() -> new NotFoundException("Recipe with id %s not found".formatted(recipeDto.getId())));
        checkThatUserIsOwnerOfRecipe(user, recipe);
        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCooking(recipeDto.getCooking());
        List<Ingredient> ingredients = updateIngredients(recipeDto.getIngredients());

        recipe.getIngredients().stream().filter(e -> !ingredients.stream().map(Ingredient::getId).toList()
                        .contains(e.getId()))
                .forEach(e -> ingredientRepository.deleteById(e.getId()));
        recipe.setIngredients(ingredients);
        recipe.setUser(userRepository.findById(recipe.getUser().getId()).orElseThrow(()
                -> new NotFoundException("User with id %s not found".formatted(recipeDto.getUserDto().getId()))));
        recipe.setCategory(getCategory(recipeDto.getCategoryDto().getId()));

        if (!recipe.getAttachment().getId().equals(recipeDto.getAttachmentDto().getId())) {
            recipe.setAttachment(attachmentService.addAttachment(multipartFile, recipeDto.getAttachmentDto().getId()));
        }

        return recipeMapper.toDto(recipeRepository.save(recipe));
    }

    private List<Ingredient> updateIngredients(List<IngredientDto> ingredientDtos) {
        List<Ingredient> ingredients = new ArrayList<>();

        for (IngredientDto ingredientDto : ingredientDtos) {
            Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientDto.getId());
            if (ingredient.isPresent()) {
                ingredient.get().setUnit(unitMapper.toEntity(ingredientDto.getUnitDto()));
                ingredient.get().setProduct(productMapper.toEntity(ingredientDto.getProductDto()));
                ingredient.get().setQuantity(ingredientDto.getQuantity());
                ingredients.add(ingredient.get());
            } else {
                ingredients.add(ingredientMapper.toEntity(ingredientDto));
            }
        }

        return ingredients;
    }

    @Override
    @Transactional
    public void deleteById(Principal principal, UUID id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe with id %s not found".formatted(id)));
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new NotFoundException("User with name %s not found"
                        .formatted(principal.getName())));
        checkThatUserIsOwnerOfRecipe(user, recipe);

        recipeRepository.deleteById(id);
    }

    private Category getCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id %s not found".formatted(categoryId)));
    }

    private List<Ingredient> createIngredients(List<IngredientDto> ingredientDtos) {

        List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientDto ingredientDto : ingredientDtos) {
            Product product = productRepository.findById(ingredientDto.getProductDto().getId())
                    .orElseThrow(() -> new NotFoundException("Product with id %s not found"
                            .formatted(ingredientDto.getProductDto().getId())));
            Unit unit = unitRepository.findById(ingredientDto.getUnitDto().getId())
                    .orElseThrow(() -> new NotFoundException("Unit with id %s not found"
                            .formatted(ingredientDto.getProductDto().getId())));
            Ingredient ingredient = new Ingredient();
            ingredient.setId(ingredientDto.getId());
            ingredient.setProduct(product);
            ingredient.setQuantity(ingredientDto.getQuantity());
            ingredient.setUnit(unit);
            ingredients.add(ingredient);
        }

        return ingredients;
    }

    private void checkThatUserIsOwnerOfRecipe(User user, Recipe recipe) {
        if (!user.getId().equals(recipe.getUser().getId())) {
            throw new NotUserRecipeException("User with name %s not owner of recipe with id %s"
                    .formatted(user.getUsername(), recipe.getId()));
        }
    }

}

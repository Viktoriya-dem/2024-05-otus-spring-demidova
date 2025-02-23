package ru.otus.recipe;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import ru.otus.recipe.dto.AttachmentDto;
import ru.otus.recipe.dto.CategoryDto;
import ru.otus.recipe.dto.IngredientDto;
import ru.otus.recipe.dto.ProductDto;
import ru.otus.recipe.dto.RecipeDto;
import ru.otus.recipe.dto.UnitDto;
import ru.otus.recipe.dto.UserDto;
import ru.otus.recipe.exception.NotUserRecipeException;
import ru.otus.recipe.mapper.AttachmentMapperImpl;
import ru.otus.recipe.mapper.CategoryMapperImpl;
import ru.otus.recipe.mapper.IngredientMapperImpl;
import ru.otus.recipe.mapper.ProductMapperImpl;
import ru.otus.recipe.mapper.RecipeMapperImpl;
import ru.otus.recipe.mapper.UnitMapperImpl;
import ru.otus.recipe.mapper.UserMapperImpl;
import ru.otus.recipe.model.Attachment;
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
import ru.otus.recipe.service.AttachmentServiceImpl;
import ru.otus.recipe.service.RecipeService;
import ru.otus.recipe.service.RecipeServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Сервис работы с рецептами должен ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class})
@Import({RecipeServiceImpl.class, RecipeMapperImpl.class, IngredientMapperImpl.class, ProductMapperImpl.class,
        CategoryMapperImpl.class, AttachmentMapperImpl.class, UnitMapperImpl.class, UserMapperImpl.class})
public class RecipeServiceImplTest {

    @Autowired
    private RecipeService recipeService;

    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private AttachmentServiceImpl attachmentService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UnitRepository unitRepository;

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private UserRepository userRepository;

    public Recipe getRecipe() {
        return new Recipe(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"), "Recipe_1",
                "Description_1", "Cooking_1",
                new Attachment(UUID.fromString("a36adce6-4b88-4ee8-a974-b3d4bd5fb3d8"),
                        "image.png"), new User(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "User_1",
                "Password_1"),
                new Category(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"), "Category_1"),
                List.of(new Ingredient(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                        new Product(UUID.fromString("1cfbce6f-49d9-4ce5-be92-2c7be9a9251b"), "Product_1"),
                        Float.parseFloat("5"), new Unit(UUID.fromString("0e6b503d-9dae-416b-add0-5053c635c3bf"),
                        "шт."))), LocalDate.now());
    }

    public List<Recipe> getRecipes() {
        return List.of(new Recipe(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"), "Recipe_1",
                "Description_1", "Cooking_1",
                new Attachment(UUID.fromString("a36adce6-4b88-4ee8-a974-b3d4bd5fb3d8"),
                        "image.png"), new User(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "User_1",
                "Password_1"),
                new Category(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"), "Category_1"),
                List.of(new Ingredient(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                        new Product(UUID.fromString("1cfbce6f-49d9-4ce5-be92-2c7be9a9251b"), "Product_1"),
                        Float.parseFloat("5"), new Unit(UUID.fromString("0e6b503d-9dae-416b-add0-5053c635c3bf"),
                        "шт."))), LocalDate.now()),
                new Recipe(UUID.fromString("652b9080-4b8b-42c0-aa09-eca5c6356f90"), "Recipe_2",
                        "Description_2", "Cooking_2",
                        new Attachment(UUID.fromString("c487e176-57cb-4f83-bb60-18a1100a0ea2"),
                                "image2.png"), new User(UUID.fromString("17d3afd5-3978-4c20-acae-6c2c75ac23b5"),
                        "User_2", "Password_2"),
                        new Category(UUID.fromString("23f017dd-e2b0-4e9c-81bd-96fa1c3730ef"), "Category_2"),
                        List.of(new Ingredient(UUID.fromString("731f4665-dd07-47eb-91d7-ce2bc4532377"),
                                new Product(UUID.fromString("d87ede9c-ecc5-4c2a-8b99-acba0e2b75d4"), "Product_2"),
                                Float.parseFloat("10"), new Unit(UUID.fromString("0e6b503d-9dae-416b-add0-5053c635c3bf"),
                                "шт."))), LocalDate.now()));
    }

    public Recipe getNewRecipe() {
        return new Recipe(UUID.fromString("8bf546a4-d193-4a6a-b782-dbe696491dfc"), "Recipe_2",
                "Description_2", "Cooking_2",
                new Attachment(UUID.fromString("3754c439-2b74-4f61-98ee-62e93092cf73"),
                        "image2.png"), new User(UUID.fromString("231bebff-c27c-428a-9848-17ca2a22f58c"), "User_2",
                "Password_2"),
                new Category(UUID.fromString("abc082cd-3e47-432f-a6a8-4dae2b13950d"), "Category_2"),
                List.of(new Ingredient(UUID.fromString("ec0fb15f-2592-44dc-8bba-072f574b64e9"),
                        new Product(UUID.fromString("c34e0b35-cdd1-4515-a283-63cc67d280a3"), "Product_2"),
                        Float.parseFloat("6"), new Unit(UUID.fromString("3eadf5d0-7e14-43bf-812b-11dca46363f1"),
                        "шт."))), LocalDate.now());
    }

    public RecipeDto getRecipeDto() {
        return new RecipeDto(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"), "Recipe_1",
                "Description_1", "Cooking_1",
                new AttachmentDto(UUID.fromString("a36adce6-4b88-4ee8-a974-b3d4bd5fb3d8"),
                "image.png"), new UserDto(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "User_1"),
                new CategoryDto(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"), "Category_1"),
                List.of(new IngredientDto(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                        new ProductDto(UUID.fromString("1cfbce6f-49d9-4ce5-be92-2c7be9a9251b"), "Product_1"),
                        Float.parseFloat("5"), new UnitDto(UUID.fromString("0e6b503d-9dae-416b-add0-5053c635c3bf"),
                        "шт."))), LocalDate.now());
    }

    public RecipeDto getNewRecipeDto() {
        return new RecipeDto(UUID.fromString("8bf546a4-d193-4a6a-b782-dbe696491dfc"), "Recipe_2",
                "Description_2", "Cooking_2",
                new AttachmentDto(UUID.fromString("3754c439-2b74-4f61-98ee-62e93092cf73"),
                        "image2.png"), new UserDto(UUID.fromString("231bebff-c27c-428a-9848-17ca2a22f58c"), "User_2"),
                new CategoryDto(UUID.fromString("abc082cd-3e47-432f-a6a8-4dae2b13950d"), "Category_2"),
                List.of(new IngredientDto(UUID.fromString("ec0fb15f-2592-44dc-8bba-072f574b64e9"),
                        new ProductDto(UUID.fromString("c34e0b35-cdd1-4515-a283-63cc67d280a3"), "Product_2"),
                        Float.parseFloat("6"), new UnitDto(UUID.fromString("3eadf5d0-7e14-43bf-812b-11dca46363f1"),
                        "шт."))), LocalDate.now());
    }

    @Test
    @DisplayName(" вернуть корректный рецепт по id")
    public void shouldReturnCorrectRecipeById() {
        when(recipeRepository.findById(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848")))
                .thenReturn(Optional.ofNullable(getRecipe()));
        RecipeDto actualRecipe = recipeService.findById(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"));
        var expectedRecipe = getRecipeDto();

        assertThat(actualRecipe).isNotNull();

        assertThat(actualRecipe).isNotNull()
                .matches(recipe -> recipe.getId() != null)
                .usingRecursiveComparison().isEqualTo(expectedRecipe);
    }

    @DisplayName("должен загружать список всех рецептов")
    @Test
    void shouldReturnCorrectRecipesList() {
        when(recipeRepository.findAll())
                .thenReturn(getRecipes());
        var actualrecipes = recipeService.findAll();

        assertThat(actualrecipes).isNotEmpty()
                .hasSize(2)
                .hasOnlyElementsOfType(RecipeDto.class);
    }

    @DisplayName("должен сохранять новый рецепт")
    @Test
    void shouldSaveNewRecipe() {
        byte[] inputArray = "Test".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("temp", inputArray);

        var expectedRecipe = getNewRecipeDto();
        when(attachmentService.addAttachment( mockMultipartFile, getNewRecipeDto().getId()))
                .thenReturn(new Attachment(UUID.fromString("3754c439-2b74-4f61-98ee-62e93092cf73"),
                        "image2.png"));
        when(userRepository.findByUsername(getNewRecipeDto().getUserDto().getUsername()))
                .thenReturn(Optional.of(new User(UUID.fromString("231bebff-c27c-428a-9848-17ca2a22f58c"), "User_2",
                        "Password_2")));
        when(productRepository.findById(getNewRecipeDto().getIngredients().get(0).getProductDto().getId()))
                .thenReturn(Optional.of(new Product(UUID.fromString("c34e0b35-cdd1-4515-a283-63cc67d280a3"), "Product_2")));
        when(unitRepository.findById(getNewRecipeDto().getIngredients().get(0).getUnitDto().getId()))
                .thenReturn(Optional.of(new Unit(UUID.fromString("3eadf5d0-7e14-43bf-812b-11dca46363f1"),
                        "шт.")));
        when(categoryRepository.findById(getNewRecipeDto().getCategoryDto().getId()))
                .thenReturn(Optional.of(new Category(UUID.fromString("abc082cd-3e47-432f-a6a8-4dae2b13950d"),
                        "Category_2")));
        when(recipeRepository.findById(getNewRecipeDto().getId()))
                .thenReturn(Optional.of(getNewRecipe()));
        when(recipeRepository.save(any()))
                .thenReturn(getNewRecipe());


        var actualRecipe = recipeService.create(getNewRecipeDto(),
                mockMultipartFile);


        assertThat(actualRecipe).isNotNull()
                .matches(recipe -> recipe.getId() != null)
                .usingRecursiveComparison().isEqualTo(expectedRecipe);
    }

    @DisplayName(" сохранять измененный рецепт")
    @Test
    void shouldSaveUpdatedRecipe() {
        var expectedRecipe = getRecipeDto();
        expectedRecipe.setName("Change_Name_Recipe_1");

        byte[] inputArray = "Test".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("temp", inputArray);

        when(recipeRepository.findById(getRecipeDto().getId()))
                .thenReturn(Optional.of(getRecipe()));
        when(userRepository.findByUsername(getRecipeDto().getUserDto().getUsername()))
                .thenReturn(Optional.of(new User(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "User_1",
                        "Password_1")));
        when(userRepository.findById(getRecipeDto().getUserDto().getId()))
                .thenReturn(Optional.of(new User(UUID.fromString("5f7019b2-382f-41fa-a8af-b46dc3e05252"), "User_1",
                        "Password_1")));
        when(ingredientRepository.findById(getRecipeDto().getIngredients().get(0).getId()))
                .thenReturn(Optional.of(new Ingredient(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                        new Product(UUID.fromString("1cfbce6f-49d9-4ce5-be92-2c7be9a9251b"), "Product_1"),
                        Float.parseFloat("5"), new Unit(UUID.fromString("0e6b503d-9dae-416b-add0-5053c635c3bf"),
                        "шт."))));
        when(categoryRepository.findById(getRecipeDto().getCategoryDto().getId()))
                .thenReturn(Optional.of(new Category(UUID.fromString("9fccd731-27a2-4639-b1f6-648087ef744b"), "Category_1")));

        assertThat(recipeService.findById(expectedRecipe.getId())).isNotNull();
        Recipe recipe = getRecipe();
         recipe.setName("Change_Name_Recipe_1");
        when(recipeRepository.save(any()))
                .thenReturn(recipe);

        var actualRecipe = recipeService.update(expectedRecipe,
                mockMultipartFile);

        assertThat(actualRecipe.getName()).isNotEqualTo(getRecipeDto().getName());
        assertThat(actualRecipe.getName()).isEqualTo(expectedRecipe.getName());
    }

    @DisplayName("не изменять рецепт, если он не принадлежит пользователю")
    @Test
    void shouldNotSaveUpdatedRecipeIfUserIsNotOwner() {
        var expectedRecipe = getRecipeDto();
        expectedRecipe.setName("Change_Name_Recipe_1");

        byte[] inputArray = "Test".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("temp", inputArray);

        when(recipeRepository.findById(getRecipeDto().getId()))
                .thenReturn(Optional.of(getRecipe()));
        when(userRepository.findByUsername(getRecipeDto().getUserDto().getUsername()))
                .thenReturn(Optional.of(new User(UUID.fromString("231bebff-c27c-428a-9848-17ca2a22f58c"), "User_2",
                        "Password_2")));
        when(ingredientRepository.findById(getRecipeDto().getIngredients().get(0).getId()))
                .thenReturn(Optional.of(new Ingredient(UUID.fromString("8b0f427f-1365-4883-8834-c6b25515b848"),
                        new Product(UUID.fromString("1cfbce6f-49d9-4ce5-be92-2c7be9a9251b"), "Product_1"),
                        Float.parseFloat("5"), new Unit(UUID.fromString("0e6b503d-9dae-416b-add0-5053c635c3bf"),
                        "шт."))));

        assertThat(recipeService.findById(expectedRecipe.getId())).isNotNull();

        assertThatThrownBy(() -> recipeService.update(expectedRecipe,
                mockMultipartFile)).isInstanceOf(NotUserRecipeException.class);

    }

}
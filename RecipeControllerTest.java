package ru.otus.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.recipe.controller.RecipeController;
import ru.otus.recipe.dto.AttachmentDto;
import ru.otus.recipe.dto.CategoryDto;
import ru.otus.recipe.dto.IngredientDto;
import ru.otus.recipe.dto.ProductDto;
import ru.otus.recipe.dto.RecipeDto;
import ru.otus.recipe.dto.UnitDto;
import ru.otus.recipe.dto.UserDto;
import ru.otus.recipe.exception.NotFoundException;
import ru.otus.recipe.security.CustomUserDetailsService;
import ru.otus.recipe.security.jwt.JwtProvider;
import ru.otus.recipe.service.AttachmentServiceImpl;
import ru.otus.recipe.service.RecipeService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер рецептов должен ")
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class})
@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttachmentServiceImpl attachmentService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private RecipeService recipeService;


    public RecipeDto getRecipeDto1() {
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

    public RecipeDto getRecipeDto2() {
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

    @DisplayName("вернуть все рецепты")
    @Test
    void shouldReturnAllRecipes() throws Exception {
        var recipeDtos = List.of(
                getRecipeDto1(),
                getRecipeDto2());

        when(recipeService.findAll()).thenReturn(recipeDtos);

        mockMvc.perform(get("/api/recipes"))
                .andExpect(content().json(mapper.writeValueAsString(recipeDtos)))
                .andExpect(status().isOk());
    }

    @DisplayName("вернуть рецепт по id")
    @Test
    void shouldReturnRecipeById() throws Exception {
        var recipeDto = getRecipeDto1();

        when(recipeService.findById(any())).thenReturn(recipeDto);

        mockMvc.perform(get("/api/recipes/" + recipeDto.getId().toString()))
                .andExpect(content().json(mapper.writeValueAsString(recipeDto)))
                .andExpect(status().isOk());
    }


    @DisplayName("вернуть ошибку 404 при неверном id рецепта")
    @Test
    void shouldNotUpdateRecipeWhenWrongRecipeId() throws Exception {
        var recipeDto = getRecipeDto1();
        when(recipeService.findById(any())).thenThrow(new NotFoundException("Recipe not found"));

        mockMvc.perform(get("/api/recipes/"  + recipeDto.getId().toString()))
                .andExpect(status().isNotFound());
    }

}
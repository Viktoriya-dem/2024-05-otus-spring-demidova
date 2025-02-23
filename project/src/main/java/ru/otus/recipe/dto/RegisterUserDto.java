package ru.otus.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    private UUID id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
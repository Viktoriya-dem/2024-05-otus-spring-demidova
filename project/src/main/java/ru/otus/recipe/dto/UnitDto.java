package ru.otus.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UnitDto {

    @NotNull
    private UUID id;

    @NotNull
    private String name;

    @Override
    public String toString() {
        return name;
    }
}

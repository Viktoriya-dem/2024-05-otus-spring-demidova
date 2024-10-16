package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private Long id;

    @NotBlank(message = "Title can't be empty")
    private String title;

    @NotNull(message = "Author can't be empty")
    private Long authorId;

    @NotNull(message = "Genres can't be empty")
    @NotEmpty(message = "Genres can't be empty")
    private Set<Long> genres;
}

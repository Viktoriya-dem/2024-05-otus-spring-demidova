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

    private long id;

    @NotBlank(message = "Заполните наименование")
    private String title;

    @NotNull(message = "Заполните автора")
    private AuthorDto author;

    private Set<@NotNull(message = "Заполните жанры")GenreDto> genres;
}

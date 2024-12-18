package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateDto {

    private UUID id;

    @NotBlank(message = "Заполните наименование")
    private String title;

    @NotNull(message = "Заполните автора")
    private AuthorDto author;

    private Set<@NotNull(message = "Заполните жанры") GenreDto> genres;
}

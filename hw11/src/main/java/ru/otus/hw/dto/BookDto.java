package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private UUID id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;
}

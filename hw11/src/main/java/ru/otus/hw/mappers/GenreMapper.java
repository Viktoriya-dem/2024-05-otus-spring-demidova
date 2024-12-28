package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
@Component
public interface GenreMapper {

    GenreDto toDto(Genre genre);

    List<GenreDto> toDto(List<Genre> genre);

    Genre toEntity(GenreDto genreDto);

    List<Genre> toEntities(List<GenreDto> genreDtos);
}
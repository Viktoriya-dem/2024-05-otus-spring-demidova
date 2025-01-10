package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
@Component
public interface AuthorMapper {

    AuthorDto toDto(Author author);

    List<AuthorDto> toDto(List<Author> author);

   Author toEntity(AuthorDto authorDto);
}
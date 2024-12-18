package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;


import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {AuthorMapper.class, GenreMapper.class})
@Component
public interface BookMapper {

    @Mapping(target = "id", source = "id")
    BookDto toDto(Book book);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "genres", ignore = true)
    default Book toEntity(BookDto bookDto, Author author, List<Genre> genres) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(author);
        book.setGenres(genres);

        return book;
    }

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "genres", ignore = true)
    default Book toEntity(BookCreateDto bookCreateDto, Author author, List<Genre> genres) {
        Book book = new Book();
        book.setId(bookCreateDto.getId());
        book.setTitle(bookCreateDto.getTitle());
        book.setAuthor(author);
        book.setGenres(genres);

        return book;
    }

    List<BookDto> toDto(List<Book> book);

    @Named("getGenresIds")
    default Set<UUID> getGenresIds(List<Genre> genres) {
        return genres.stream().map(Genre::getId).collect(Collectors.toSet());
    }

    @Named("getGenresNames")
    default String getGenresNames(List<Genre> genres) {
        return genres.stream().map(Genre::getName).collect(Collectors.joining(","));
    }

}

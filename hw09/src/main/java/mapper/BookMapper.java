package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "genres", qualifiedByName = "getGenresIds", source = "genres")
    BookDto toDto(Book book);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "genres", ignore = true)
    Book toEntity(BookDto bookDto);

    @Named("getGenresIds")
    default Set<Long> getGenresIds(List<Genre> genres) {
        return genres.stream().map(Genre::getId).collect(Collectors.toSet());
    }

}

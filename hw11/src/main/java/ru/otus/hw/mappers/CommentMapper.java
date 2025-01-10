package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {BookMapper.class})
@Component
public interface CommentMapper {

    @Mapping(target = "bookId", source = "book.id")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDto(List<Comment> comment);

}
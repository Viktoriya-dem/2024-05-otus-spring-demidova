package ru.otus.recipe.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.otus.recipe.dto.AttachmentDto;
import ru.otus.recipe.model.Attachment;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
@Component
public interface AttachmentMapper {

    AttachmentDto toDto(Attachment attachment);

    List<AttachmentDto> toDto(List<Attachment> attachments);

    Attachment toEntity(AttachmentDto productDto);
}
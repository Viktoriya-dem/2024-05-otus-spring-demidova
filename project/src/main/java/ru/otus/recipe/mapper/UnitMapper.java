package ru.otus.recipe.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.otus.recipe.dto.UnitDto;
import ru.otus.recipe.model.Unit;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
@Component
public interface UnitMapper {

    UnitDto toDto(Unit unit);

    List<UnitDto> toDto(List<Unit> units);

    Unit toEntity(UnitDto unitDto);
}
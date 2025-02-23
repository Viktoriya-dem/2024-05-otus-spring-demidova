package ru.otus.recipe.service;

import ru.otus.recipe.dto.UnitDto;

import java.util.List;

public interface UnitService {

    List<UnitDto> findAll();

}

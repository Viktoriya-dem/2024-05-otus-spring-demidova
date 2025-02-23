package ru.otus.recipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.recipe.dto.UnitDto;
import ru.otus.recipe.service.UnitService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @GetMapping("/api/units")
    public List<UnitDto> getAllUnits() {

        return unitService.findAll();
    }

}

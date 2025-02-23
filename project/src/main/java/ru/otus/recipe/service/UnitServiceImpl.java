package ru.otus.recipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.recipe.dto.UnitDto;
import ru.otus.recipe.mapper.UnitMapper;
import ru.otus.recipe.repository.UnitRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;

    private final UnitMapper unitMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UnitDto> findAll() {
        return unitMapper.toDto(unitRepository.findAll());
    }

}

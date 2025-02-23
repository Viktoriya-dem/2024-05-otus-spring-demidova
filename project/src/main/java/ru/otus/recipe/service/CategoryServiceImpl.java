package ru.otus.recipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.recipe.dto.CategoryDto;
import ru.otus.recipe.mapper.CategoryMapper;
import ru.otus.recipe.repository.CategoryRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryMapper.toDto(categoryRepository.findAll());
    }

}

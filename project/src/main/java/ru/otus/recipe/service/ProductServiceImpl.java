package ru.otus.recipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.recipe.dto.ProductDto;
import ru.otus.recipe.mapper.ProductMapper;
import ru.otus.recipe.repository.ProductRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return productMapper.toDto(productRepository.findAll());
    }

}

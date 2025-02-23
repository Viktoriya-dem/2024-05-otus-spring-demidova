package ru.otus.recipe.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.otus.recipe.dto.ProductDto;
import ru.otus.recipe.model.Product;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
@Component
public interface ProductMapper {

    ProductDto toDto(Product product);

    List<ProductDto> toDto(List<Product> products);

    Product toEntity(ProductDto productDto);
}
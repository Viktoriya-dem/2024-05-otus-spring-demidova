package ru.otus.recipe.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.otus.recipe.dto.RegisterUserDto;
import ru.otus.recipe.dto.UserDto;
import ru.otus.recipe.model.User;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
@Component
public interface UserMapper {

    UserDto toDto(User user);

    List<UserDto> toDto(List<User> users);

    User toEntity(UserDto userDto);

    User toEntity(RegisterUserDto userDto);
}
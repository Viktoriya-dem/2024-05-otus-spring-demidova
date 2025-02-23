package ru.otus.recipe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.recipe.dto.RegisterUserDto;
import ru.otus.recipe.exception.NotFoundException;
import ru.otus.recipe.mapper.UserMapper;
import ru.otus.recipe.model.User;
import ru.otus.recipe.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public User findByLogin(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with name %s not found"
                        .formatted(username)));
    }


    public User findByNameAndPassword(String username, String password) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            log.error("User not found in the Database");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found in the Database: {}", username);
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return user.get();
            }
        }
        return null;
    }

    @Transactional
    public void saveUser(RegisterUserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}

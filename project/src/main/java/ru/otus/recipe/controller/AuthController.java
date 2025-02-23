package ru.otus.recipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.recipe.dto.RegisterUserDto;
import ru.otus.recipe.model.User;
import ru.otus.recipe.security.jwt.JwtProvider;
import ru.otus.recipe.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    private final JwtProvider jwtProvider;

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User user = userService.findByNameAndPassword(request.getName(), request.getPassword());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        String token = jwtProvider.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody RegisterUserDto userDto) {
        userService.saveUser(userDto);
    }
}

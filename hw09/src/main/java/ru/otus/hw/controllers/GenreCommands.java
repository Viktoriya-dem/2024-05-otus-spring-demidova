package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class GenreCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @GetMapping("/genres")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}

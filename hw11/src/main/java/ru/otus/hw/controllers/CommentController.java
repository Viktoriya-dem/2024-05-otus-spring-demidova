package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.repositories.CommentRepository;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @GetMapping("/api/comments/book/{id}")
    public Flux<CommentDto> getComments(@PathVariable UUID id) {

        return commentRepository.findAllByBookId(id).map(commentMapper::toDto);
    }

}

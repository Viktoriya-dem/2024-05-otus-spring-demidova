package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.controllers.CommentController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Контроллер комментариев должен ")
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    private CommentMapper commentMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    void setup() {
        commentMapper = Mappers.getMapper(CommentMapper.class);
    }


    @DisplayName("вернуть комментарии")
    @Test
    void shouldReturnAllComments() throws Exception {
        var commentDtos = List.of(
                new CommentDto(1L, "Comment_1", 1L),
                new CommentDto(2L, "Comment_2", 1L)
        );

        when(commentService.findAllByBookId(1L)).thenReturn(commentDtos);

        mockMvc.perform(get("/comments/book/%d" .formatted(1)))
                .andExpect(content().json(mapper.writeValueAsString(commentDtos)))
                .andExpect(status().isOk());
    }

    @DisplayName("создать комментарий")
    @Test
    void shouldCreateComment() throws Exception {
        var commentDto = new CommentDto(2L, "Comment_2", 1L);

        when(commentService.create(commentDto.getText(), commentDto.getBookId())).thenReturn(commentDto);

        var content = post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commentDto));


        mockMvc.perform(content)
                .andExpect(status().isCreated());
    }

    @DisplayName("обновить комментарий")
    @Test
    void shouldUpdateComment() throws Exception {
        var commentDto = new CommentDto(2L, "Comment_2", 1L);

        when(commentService.update(commentDto.getId(), commentDto.getText())).thenReturn(commentDto);

        var content = patch("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commentDto));

        mockMvc.perform(content)
                .andExpect(status().isOk());
    }

    @DisplayName("удалить комментарий")
    @Test
    void shouldDeleteComment() throws Exception {
        doNothing().when(commentService);

        var content = delete("/comments/%d" .formatted(1));

        mockMvc.perform(content)
                .andExpect(status().isNoContent());
    }


}
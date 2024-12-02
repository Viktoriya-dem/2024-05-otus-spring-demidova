package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private UUID id;

    @NotBlank(message = "Заполните текст комментария")
    private String text;

    private UUID bookId;

}

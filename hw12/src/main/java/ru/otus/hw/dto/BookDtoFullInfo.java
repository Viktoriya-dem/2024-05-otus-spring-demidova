package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoFullInfo {

    private Long id;

    private String title;

    private String authorName;

    private String genres;
}

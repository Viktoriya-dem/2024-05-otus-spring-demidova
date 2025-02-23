package ru.otus.recipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.recipe.service.AttachmentService;

@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/api/attachment/{name}")
    public byte[] getFileByName(@PathVariable String name) {

        return attachmentService.findAttachByName(name);
    }

}

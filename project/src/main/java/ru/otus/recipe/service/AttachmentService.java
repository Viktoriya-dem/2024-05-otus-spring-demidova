package ru.otus.recipe.service;

import org.springframework.web.multipart.MultipartFile;
import ru.otus.recipe.model.Attachment;

import java.util.UUID;

public interface AttachmentService {

    Attachment addAttachment(MultipartFile file, UUID id);

    byte[] findAttachByName(String name);

}

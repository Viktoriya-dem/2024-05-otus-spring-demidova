package ru.otus.recipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.recipe.model.Attachment;
import ru.otus.recipe.repository.AttachmentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public Attachment addAttachment(MultipartFile file, UUID id) {

        File uploadDir = new File("img");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String currentDate = new SimpleDateFormat("yyyyy-mm-dd-hh-mm-ss").format(new Date());

        String fileName =
                "img_" + currentDate + "_" + Objects.requireNonNull(file.getOriginalFilename())
                        .toLowerCase().replaceAll(" ", "-");
        try {
            File newFile = new File(uploadDir + "/" + fileName);
            Files.write(newFile.toPath(), file.getBytes());
            Attachment attachment = new Attachment(id, fileName);
            attachmentRepository.save(attachment);
            return attachment;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] findAttachByName(String name) {
        File file = new File("img/" + name);
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

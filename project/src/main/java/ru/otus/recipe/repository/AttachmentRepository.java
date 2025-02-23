package ru.otus.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.recipe.model.Attachment;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}

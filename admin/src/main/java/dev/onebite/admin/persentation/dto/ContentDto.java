package dev.onebite.admin.persentation.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public record ContentDto(
        Long contentId,
        String type,
        String title,
        String code,
        String description,
        String answer,
        String beforeCode,
        String afterCode,
        String feedback,
        String imageUrl,
        String question,
        Integer views,
        Integer bookmarks,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {
}
package dev.onebite.admin.persentation.dto.request;

import java.util.List;

public record UpdateContentCommand(
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
        String questionText,
        String language,
        List<String> tags,
        List<String> tails
) {
}

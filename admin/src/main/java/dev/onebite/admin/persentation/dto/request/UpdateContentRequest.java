package dev.onebite.admin.persentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateContentRequest(

        @NotBlank(message = "콘텐츠 타입은 필수입니다")
        @Size(max = 20, message = "콘텐츠 타입은 20자 이하여야 합니다")
        String type,

        @NotBlank(message = "제목은 필수입니다")
        @Size(max = 50, message = "제목은 50자 이하여야 합니다")
        String title,

        String code,
        String description,
        String answer,
        String beforeCode,
        String afterCode,
        String feedback,

        @Pattern(
                regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$",
                message = "올바른 URL 형식이 아닙니다"
        )
        @Size(max = 500, message = "이미지 URL은 500자 이하여야 합니다")
        String imageUrl,

        String question
) {
}

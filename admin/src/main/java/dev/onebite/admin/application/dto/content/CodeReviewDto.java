package dev.onebite.admin.application.dto.content;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dev.onebite.admin.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "type", "title", "language", "before", "after", "feedback", "tags", "createdAt"})
public class CodeReviewDto extends ContentDto {
    private String before;
    private String after;
    private String feedback;
    private String language;

    public static CodeReviewDto from(Content content) {
        CodeReviewDto dto = new CodeReviewDto(
                content.getBeforeCode(),
                content.getAfterCode(),
                content.getFeedback(),
                content.getLanguage()
        );
        dto.id = content.getId();
        dto.type = "code_review";
        dto.title = content.getTitle();
        dto.createdAt = String.valueOf(content.getCreatedAt());
        return dto;
    }
}

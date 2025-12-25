package dev.onebite.admin.application.dto.content;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dev.onebite.admin.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "type", "title", "code", "language", "description", "tags", "createdAt"})
public class CodeTipDto extends ContentDto {

    private String code;
    private String language;
    private String description;

    public static CodeTipDto from(Content content) {
        CodeTipDto dto = new CodeTipDto(
                content.getCode(),
                content.getLanguage(),
                content.getDescription()
        );
        dto.id = content.getId();
        dto.type = "code_tip";
        dto.title = content.getTitle();
        dto.createdAt = String.valueOf(content.getCreatedAt());
        return dto;
    }
}

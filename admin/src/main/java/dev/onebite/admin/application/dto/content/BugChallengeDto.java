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
@JsonPropertyOrder({"id", "type", "title", "code", "language", "answer", "tags", "createdAt"})
public class BugChallengeDto extends ContentDto {

    private String code;
    private String answer;
    private String language;

    public static BugChallengeDto from(Content content) {
        BugChallengeDto dto = new BugChallengeDto(
                content.getCode(),
                content.getAnswer(),
                content.getLanguage()
        );
        dto.id = content.getId();
        dto.type = "bug_challenge";
        dto.title = content.getTitle();
        dto.createdAt = String.valueOf(content.getCreatedAt());
        return dto;
    }
}

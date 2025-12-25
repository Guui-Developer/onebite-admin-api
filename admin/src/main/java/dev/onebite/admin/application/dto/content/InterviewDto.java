package dev.onebite.admin.application.dto.content;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dev.onebite.admin.domain.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "type", "title", "question", "answer", "tails", "tags", "createdAt"})
public class InterviewDto extends ContentDto {
    private String question;
    private String answer;
    private List<String> tails;

    public static InterviewDto from(Content content) {
        InterviewDto dto = new InterviewDto(
                content.getQuestionText(),
                content.getAnswer(),
                List.of()
        );
        dto.id = content.getId();
        dto.type = "interview";
        dto.title = content.getTitle();
        dto.createdAt = String.valueOf(content.getCreatedAt());
        return dto;
    }
}

package dev.onebite.admin.application.dto.content;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import dev.onebite.admin.domain.Content;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static dev.onebite.admin.infra.enums.ContentType.*;


@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CodeTipDto.class, name = "code_tip"),
        @JsonSubTypes.Type(value = BugChallengeDto.class, name = "bug_challenge"),
        @JsonSubTypes.Type(value = CodeReviewDto.class, name = "code_review"),
        @JsonSubTypes.Type(value = MemeDto.class, name = "meme"),
        @JsonSubTypes.Type(value = InterviewDto.class, name = "interview")
})
@JsonPropertyOrder({"id", "type", "title", "tags", "createdAt"})
public abstract class ContentDto {
    protected Long id;
    protected String type;
    protected String title;
    protected List<String> tags;
    protected String createdAt;

    public static ContentDto from(Content content) {
        return switch (content.getType()) {
            case "code_tip" -> CodeTipDto.from(content);
            case "bug_challenge" -> BugChallengeDto.from(content);
            case "code_review" -> CodeReviewDto.from(content);
            case "meme" -> MemeDto.from(content);
            case "interview" -> InterviewDto.from(content);
            default -> throw new IllegalStateException("존재하지 않는 컨텐츠 타입입니다.: " + content.getType());
        };

    }

    public void assignTags(List<String> tag) {
        this.tags = tag;
    }
}

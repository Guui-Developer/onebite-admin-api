package dev.onebite.admin.persentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ContentDto {

    private final Long contentId;
    private final String type;
    private final String title;
    private final String code;
    private final String description;
    private final String answer;
    private final String beforeCode;
    private final String afterCode;
    private final String feedback;
    private final String imageUrl;
    private final String question;
    private final Integer views;
    private final Integer bookmarks;
    private final Boolean isActive;
    private final Instant createdAt;
    private final Instant updatedAt;

    private List<String> tag = new ArrayList<>();


    public ContentDto(Long contentId, String type, String title, String code,
                      String description, String answer, String beforeCode,
                      String afterCode, String feedback, String imageUrl,
                      String questionText, Integer views, Integer bookmarks,
                      Boolean isActive, Instant createdAt, Instant updatedAt) {
        this.contentId = contentId;
        this.type = type;
        this.title = title;
        this.code = code;
        this.description = description;
        this.answer = answer;
        this.beforeCode = beforeCode;
        this.afterCode = afterCode;
        this.feedback = feedback;
        this.imageUrl = imageUrl;
        this.question = questionText;
        this.views = views;
        this.bookmarks = bookmarks;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void assignTags(List<String> tag) {
        this.tag = tag;
    }
}
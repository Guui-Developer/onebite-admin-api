package dev.onebite.admin.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentEditor {
    private String type;
    private String title;
    private String code;
    private String description;
    private String answer;
    private String beforeCode;
    private String afterCode;
    private String feedback;
    private String imageUrl;
    private String questionText;
}

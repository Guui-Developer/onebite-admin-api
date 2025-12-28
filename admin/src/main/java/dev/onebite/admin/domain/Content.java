package dev.onebite.admin.domain;

import dev.onebite.admin.domain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @Column(name = "before_code", columnDefinition = "TEXT")
    private String beforeCode;

    @Column(name = "after_code", columnDefinition = "TEXT")
    private String afterCode;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "question", columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "views", columnDefinition = "int4 default 0")
    private Integer views;

    @Column(name = "bookmarks", columnDefinition = "int4 default 0")
    private Integer bookmarks;

    @Column(name = "language", length = 30)
    private String language;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> tails = new ArrayList<>();

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Content(String type, String title, String code, String description, String answer, String beforeCode, String afterCode, String feedback, String imageUrl, String questionText, String language, List<String> tails) {
        this.type = type;
        this.title = title;
        this.code = code;
        this.description = description;
        this.answer = answer;
        this.beforeCode = beforeCode;
        this.afterCode = afterCode;
        this.feedback = feedback;
        this.imageUrl = imageUrl;
        this.questionText = questionText;
        this.language = language;
        this.tails = tails;
        this.views = 0;
        this.bookmarks = 0;
    }

    public static Content of(String type, String title, String code, String description, String answer, String beforeCode, String afterCode, String feedback, String imageUrl, String questionText, String language, List<String> tails) {
        return new Content(type, title, code, description, answer, beforeCode, afterCode, feedback, imageUrl, questionText, language, tails);
    }

    public ContentEditor.ContentEditorBuilder toEditor() {
        return ContentEditor.builder()
                .type(this.type)
                .title(this.title)
                .code(this.code)
                .description(this.description)
                .answer(this.answer)
                .beforeCode(this.beforeCode)
                .afterCode(this.afterCode)
                .feedback(this.feedback)
                .imageUrl(this.imageUrl)
                .questionText(this.questionText)
                .language(this.language)
                .tails(this.tails);
    }


    public void edit(ContentEditor editor) {
        this.type = editor.getType();
        this.title = editor.getTitle();
        this.code = editor.getCode();
        this.description = editor.getDescription();
        this.answer = editor.getAnswer();
        this.beforeCode = editor.getBeforeCode();
        this.afterCode = editor.getAfterCode();
        this.feedback = editor.getFeedback();
        this.imageUrl = editor.getImageUrl();
        this.questionText = editor.getQuestionText();
        this.language = editor.getLanguage();
        this.tails = editor.getTails();
    }
}

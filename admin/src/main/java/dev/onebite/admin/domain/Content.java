package dev.onebite.admin.domain;

import dev.onebite.admin.domain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_key")
    private CategoryGroup categoryKey;

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

    @Column(name = "company", length = 100)
    private String company;

    @Column(name = "tags", columnDefinition = "jsonb")
    private String tags;

    @Column(name = "views", columnDefinition = "int4 default 0")
    private Integer views;

    @Column(name = "bookmarks", columnDefinition = "int4 default 0")
    private Integer bookmarks;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}

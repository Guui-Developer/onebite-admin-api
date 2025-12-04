package dev.onebite.admin.domain;

import dev.onebite.admin.domain.global.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "categories")
@ToString
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Size(max = 100)
    @NotNull
    @Column(name = "label", nullable = false, length = 100)
    private String label;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_group_id")
    @ToString.Exclude
    private CategoryGroup categoryGroup;

    @Size(max = 500)
    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("0")
    @Column(name = "display_order")
    private Integer displayOrder;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @OneToMany(mappedBy = "categoryId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CategoryContent> categoryContents = new ArrayList<>();

    private Category(String code, String label, CategoryGroup categoryGroup, String iconUrl, String description, Integer displayOrder) {
        this.code = code;
        this.label = label;
        this.categoryGroup = categoryGroup;
        this.iconUrl = iconUrl;
        this.description = description;
        this.displayOrder = displayOrder;
        this.isActive = true;
    }

    public static Category of(String code, String label, CategoryGroup categoryGroup, String iconUrl, String description, Integer displayOrder) {
        return new Category(code, label, categoryGroup, iconUrl, description, displayOrder);
    }
}
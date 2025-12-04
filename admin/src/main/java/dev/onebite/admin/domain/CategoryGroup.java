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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category_groups")
public class CategoryGroup extends BaseEntity {

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

    @Size(max = 500)
    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    @Column(name = "display_order")
    private Integer displayOrder;

    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Category> categories = new ArrayList<>();

    private CategoryGroup(String code, String label, String iconUrl, Integer displayOrder) {
        this.code = code;
        this.label = label;
        this.iconUrl = iconUrl;
        this.displayOrder = displayOrder;
    }

    public static CategoryGroup of(String code, String label, String iconUrl, Integer displayOrder) {
        return new CategoryGroup(code, label, iconUrl, displayOrder);
    }
}
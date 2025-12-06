package dev.onebite.admin.domain;

import dev.onebite.admin.domain.global.BaseEntity;
import dev.onebite.admin.infra.enums.ErrorCode;
import dev.onebite.admin.persentation.exception.ApplicationException;
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
        validateCode(code);
        validateLabel(label);

        this.code = code;
        this.label = label;
        this.iconUrl = iconUrl;
        this.displayOrder = displayOrder;
    }

    public static CategoryGroup of(String code, String label, String iconUrl, Integer displayOrder) {
        return new CategoryGroup(code, label, iconUrl, displayOrder);
    }

    private void validateCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ApplicationException(ErrorCode.VALIDATION_ERROR);
        }
        if (code.length() < 2 || code.length() > 20) {
            throw new ApplicationException(ErrorCode.INVALID_CODE_LENGTH);
        }
    }

    private void validateLabel(String label) {
        if (label == null || label.isBlank()) {
            throw new ApplicationException(ErrorCode.VALIDATION_ERROR);
        }
        if (label.length() < 2 || label.length() > 20) {
            throw new ApplicationException(ErrorCode.INVALID_CODE_LENGTH);
        }
    }

    public ProductEditor.ProductEditorBuilder toEditor() {
        return ProductEditor.builder()
                .code(this.code)
                .label(this.label)
                .iconUrl(this.iconUrl)
                .displayOrder(this.displayOrder);
    }

    public void edit(ProductEditor editor) {
        this.code = editor.getCode();
        this.label = editor.getLabel();
        this.iconUrl = editor.getIconUrl();
        this.displayOrder = editor.getDisplayOrder();
    }
}
package dev.onebite.admin.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductEditor {
    private String code;
    private String label;
    private String iconUrl;
    private int displayOrder;
}

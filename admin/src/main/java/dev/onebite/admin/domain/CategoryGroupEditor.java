package dev.onebite.admin.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryGroupEditor {
    private String code;
    private String label;
    private String iconUrl;
}

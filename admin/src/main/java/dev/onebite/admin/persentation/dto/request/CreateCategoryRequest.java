package dev.onebite.admin.persentation.dto.request;

public record CreateCategoryRequest(
        Long categoryGroupId,
        String code,
        String label,
        String iconUrl,
        String description
) {
}

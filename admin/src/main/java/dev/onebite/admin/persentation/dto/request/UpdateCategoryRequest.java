package dev.onebite.admin.persentation.dto.request;

public record UpdateCategoryRequest(
        Long categoryGroupId,
        String code,
        String label,
        String iconUrl
) {
}

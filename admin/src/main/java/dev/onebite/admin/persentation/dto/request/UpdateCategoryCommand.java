package dev.onebite.admin.persentation.dto.request;

public record UpdateCategoryCommand(
        Long id,
        String code,
        String label,
        Long categoryGroupId,
        String iconUrl) {
}

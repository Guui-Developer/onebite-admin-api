package dev.onebite.admin.persentation.dto.request;

public record UpdateCategoryGroupRequest(
        String groupCode,
        String groupLabel,
        String iconUrl
) {
}

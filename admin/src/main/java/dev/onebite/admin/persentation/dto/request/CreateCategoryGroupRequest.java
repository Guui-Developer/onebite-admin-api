package dev.onebite.admin.persentation.dto.request;

public record CreateCategoryGroupRequest(
        String groupCode,
        String groupLabel,
        String iconUrl
) {
}

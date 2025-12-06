package dev.onebite.admin.persentation.dto.request;

public record UpdateCategoryGroupRequest(
        Long groupId,
        String groupCode,
        String groupLabel,
        String iconUrl,
        int displayOrder
) {
}

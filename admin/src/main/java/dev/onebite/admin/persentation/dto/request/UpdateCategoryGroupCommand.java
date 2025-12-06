package dev.onebite.admin.persentation.dto.request;

public record UpdateCategoryGroupCommand(
        Long groupId,
        String groupCode,
        String groupLabel,
        String iconUrl,
        int displayOrder
) {
}

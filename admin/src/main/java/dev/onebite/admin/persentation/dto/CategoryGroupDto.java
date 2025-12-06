package dev.onebite.admin.persentation.dto;

public record CategoryGroupDto(Long groupId,
                               String groupCode,
                               String groupLabel,
                               String iconUrl,
                               Integer displayOrder) {
}

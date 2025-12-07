package dev.onebite.admin.persentation.dto;

public record CategoryDto(Long categoryId,
                          String code,
                          String label,
                          Long categoryGroupId,
                          String iconUrl,
                          Integer displayOrder) {
}

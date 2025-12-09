package dev.onebite.admin.application.dto;

public record AuthToken(
        String accessToken,
        long expiresIn,
        long accessExpiresAt
) {
}

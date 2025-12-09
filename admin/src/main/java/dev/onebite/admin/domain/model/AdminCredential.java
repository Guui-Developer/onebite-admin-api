package dev.onebite.admin.domain.model;

public record AdminCredential(String loginId, String password) {

    public AdminCredential {
        if (loginId == null || loginId.isBlank()) {
            throw new IllegalArgumentException("Admin ID cannot be empty");
        }
    }

    public boolean matchLoginId(String loginId) {
        return this.loginId.equals(loginId);
    }

    public boolean matchPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}

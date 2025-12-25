package dev.onebite.admin.infra.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ContentType {
    CODE_TIP,
    BUG_CHALLENGE,
    CODE_REVIEW,
    MEME,
    INTERVIEW;

    @JsonCreator
    public static ContentType from(String value) {
        return valueOf(value.toUpperCase());
    }
}

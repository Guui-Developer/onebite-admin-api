package dev.onebite.admin.persentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDetail {
    private String code;
    private String message;

    public static ErrorDetail of(String code, String message) {
        return new ErrorDetail(code, message);
    }
}

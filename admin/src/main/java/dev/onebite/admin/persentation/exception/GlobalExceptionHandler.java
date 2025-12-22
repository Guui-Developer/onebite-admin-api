package dev.onebite.admin.persentation.exception;

import dev.onebite.admin.persentation.dto.response.ErrorDetail;
import dev.onebite.admin.persentation.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ErrorResponse.of(ErrorDetail.of("VALIDATION_ERROR", "필수 유효성 검사 실패", errors));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApplicationException.class)
    public ErrorResponse handleIllegalArgumentException(ApplicationException e) {
        return ErrorResponse.of(ErrorDetail.of(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
    }

    /**
     * [500] 나머지 모든 예외 (최후의 보루)
     * 가장 구체적인 예외부터 체크하므로, Exception.class는 자동으로 가장 마지막 순위가 됩니다.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 에러
    public ErrorResponse handleException(Exception e) {
        log.error("[Unexpected error occurred", e);

        return ErrorResponse.of(
                ErrorDetail.of(
                        "INTERNAL_SERVER_ERROR",
                        "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("[Invalid Argument] {}", e.getMessage());
        return ErrorResponse.of(ErrorDetail.of("INVALID_ARGUMENT", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleJsonException(HttpMessageNotReadableException e) {
        log.warn("[Invalid JSON format] {}", e.getMessage());
        return ErrorResponse.of(ErrorDetail.of("INVALID_JSON_FORMAT", "요청 데이터 형식이 올바르지 않습니다."));
    }
}

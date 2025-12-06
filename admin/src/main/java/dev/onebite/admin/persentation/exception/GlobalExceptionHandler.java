package dev.onebite.admin.persentation.exception;

import dev.onebite.admin.persentation.dto.response.ErrorDetail;
import dev.onebite.admin.persentation.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ErrorResponse handleIllegalArgumentException(ApplicationException e) {
        return ErrorResponse.of(ErrorDetail.of(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
    }
}

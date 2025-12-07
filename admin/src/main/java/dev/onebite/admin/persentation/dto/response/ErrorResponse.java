package dev.onebite.admin.persentation.dto.response;



public record ErrorResponse(boolean success, ErrorDetail error) {

    public static ErrorResponse of(ErrorDetail error) {
        return new ErrorResponse(false, error);
    }

}

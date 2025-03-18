package io.quacker.global.exception.dto;

public record ErrorResponse(String errorMessage, int httpStatusCode) {

    public static ErrorResponse of(String errorMessage, int httpStatusCode) {
        return new ErrorResponse(errorMessage, httpStatusCode);
    }

}

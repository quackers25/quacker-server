package io.quacker.global.exception;

import io.quacker.global.exception.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(ex.getErrorMessage(), ex.getHttpStatusCode());
        return ResponseEntity.status(errorResponse.httpStatusCode()).body(errorResponse);
    }
}

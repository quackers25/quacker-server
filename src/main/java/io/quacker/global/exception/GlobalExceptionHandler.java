package io.quacker.global.exception;

import io.quacker.global.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(ex.getErrorMessage(), ex.getHttpStatusCode());
        return ResponseEntity.status(errorResponse.httpStatusCode()).body(errorResponse);
    }

    // Spring valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationExceptionsHandler(MethodArgumentNotValidException ex) {
        // 첫 번째 에러만 꺼내서 CustomException으로 감쌈
        FieldError fieldError = ex.getBindingResult().getFieldError();

        String message = fieldError != null ? fieldError.getDefaultMessage() : "Validation error";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(message, HttpStatus.BAD_REQUEST.value()));
    }

}

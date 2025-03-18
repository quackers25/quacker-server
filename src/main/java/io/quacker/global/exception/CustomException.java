package io.quacker.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final String errorMessage;
    private final int httpStatusCode;
}

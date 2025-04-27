package com.pmolinav.users.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomStatusException extends RuntimeException {
    private final HttpStatus statusCode;

    public CustomStatusException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public CustomStatusException(String message, int statusCode) {
        super(message);
        this.statusCode = HttpStatus.valueOf(statusCode);
    }
}

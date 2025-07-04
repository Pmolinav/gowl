package com.pmolinav.prediction.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomStatusException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

package com.pmolinav.prediction.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends CustomStatusException {

    public InternalServerErrorException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

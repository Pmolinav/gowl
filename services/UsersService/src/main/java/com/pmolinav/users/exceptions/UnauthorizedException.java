package com.pmolinav.users.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomStatusException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

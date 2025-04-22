package net.pmolinav.bookingslib.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomStatusException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

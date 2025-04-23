package com.pmolinav.bookings.controller;

import com.pmolinav.userslib.exception.CustomStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(CustomStatusException.class)
    public ResponseEntity<String> handleUnexpectedException(CustomStatusException e) {
        logError(e);
        return ResponseEntity.status(e.getStatusCode()).build();
    }

    private void logError(Exception e) {
        String controllerName = getControllerName();
        Logger logger = LoggerFactory.getLogger(controllerName);

        logger.error("Unexpected error while executing controller {}: {}", controllerName, e.getMessage(), e);
    }

    private String getControllerName() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String handler = (String) requestAttributes.getRequest().getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");

        // We assume that the controller name is the last segment in the controller's path.
        String[] segments = handler.split("/");
        return segments[segments.length - 1];
    }
}

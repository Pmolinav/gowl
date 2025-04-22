package net.pmolinav.configuration.controller;

import net.pmolinav.bookingslib.dto.ApiError;
import net.pmolinav.bookingslib.dto.MDCKeys;
import net.pmolinav.bookingslib.exception.CustomStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void setupMdcAndValidateRequestUid(@Nullable @RequestParam String requestUid) {
        MDC.put(MDCKeys.requestUid.name(), requestUid);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(CustomStatusException e,
                                                              WebRequest request,
                                                              HttpServletRequest httpServletRequest) {

        return logErrorAndReturn(e, request, httpServletRequest);
    }

    private ResponseEntity<ApiError> logErrorAndReturn(Exception e,
                                                       WebRequest request,
                                                       HttpServletRequest httpServletRequest) {
        String controllerName = getControllerName(httpServletRequest);
        Logger logger = LoggerFactory.getLogger(controllerName);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String error = "Internal Server Error";
        String message = "An unexpected error occurred. Please try again later.";

        if (e instanceof CustomStatusException customStatusException) {
            status = customStatusException.getStatusCode();
            message = customStatusException.getMessage();
            error = status.getReasonPhrase();
        }
        ApiError apiError = new ApiError(status.value(), error, message, httpServletRequest.getRequestURI());
        // Log will be different depending on error codes.
        if (status.value() >= 500) {
            logger.error("An unexpected error occurred while executing controller {}. Error response: {}", controllerName, apiError, e);
        } else {
            logger.warn("An error occurred while executing controller {}. Error response: {}", controllerName, apiError, e);
        }
        return new ResponseEntity<>(apiError, status);
    }

    private String getControllerName(HttpServletRequest httpServletRequest) {
        HandlerMethod handlerMethod = (HandlerMethod) httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

        String controllerName = "Unknown";
        if (handlerMethod != null) {
            controllerName = handlerMethod.getBeanType().getSimpleName();
        }
        return controllerName;
    }
}

//package com.pmolinav.leagues.controllers;
//
//import com.pmolinav.leagues.exceptions.CustomStatusException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@ControllerAdvice
//public class GlobalControllerAdvice {
//
//    @ExceptionHandler(CustomStatusException.class)
//    public ResponseEntity<String> handleUnexpectedException(CustomStatusException e) {
//        logError(e);
//        return ResponseEntity.status(e.getStatusCode()).build();
//    }
//
//    private void logError(Exception e) {
//        String controllerName = getControllerName();
//        Logger logger = LoggerFactory.getLogger(controllerName);
//
//        logger.error("Unexpected error while executing controller {}: {}", controllerName, e.getMessage(), e);
//    }
//    private String getControllerName() {
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//
//        Object handler = requestAttributes.getRequest().getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
//
//        if (handler instanceof org.springframework.web.method.HandlerMethod handlerMethod) {
//            return handlerMethod.getBeanType().getSimpleName(); // Te da el nombre de la clase del Controller
//        }
//
//        return "UnknownController";
//    }
//
//}

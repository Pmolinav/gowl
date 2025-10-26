package com.pmolinav.prediction.controllers;


import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.prediction.services.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO: ORDER SWAGGER WITHOUT USING NUMBERS IN TAGS TO PUT LOGIN AT FIRST.
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@SecurityRequirement(name = "BearerToken")
@RequestMapping("health")
@Tag(name = "1. Health", description = "The Health Controller. It can be used to check the application status.")
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    private HealthService healthService;

    @GetMapping()
    @Operation(summary = "Health check", description = "This endpoint will notify us if needed services are UP or KO.")
    public ResponseEntity<String> health(@RequestParam String requestUid) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("HealthController: health.");

            healthService.health();
            return ResponseEntity.ok("UP");
        } catch (Exception e) {
            return new ResponseEntity<>("KO", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }
}

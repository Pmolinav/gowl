package com.pmolinav.auth.controllers;

import com.pmolinav.auth.services.AuthHealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO: ORDER SWAGGER WITHOUT USING NUMBERS IN TAGS TO PUT LOGIN AT FIRST.
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("health")
@Tag(name = "1. Health", description = "The Health Controller. It can be used to check the application status.")
public class HealthController {

    @Autowired
    private AuthHealthService authHealthService;

    @GetMapping()
    @Operation(summary = "Health check", description = "This endpoint will notify us if needed services are UP or KO.")
    public ResponseEntity<String> health(@RequestParam String requestUid) {
        try {
            authHealthService.health();

            return ResponseEntity.ok("UP");
        } catch (Exception e) {
            return new ResponseEntity<>("KO", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

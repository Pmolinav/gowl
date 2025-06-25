package com.pmolinav.prediction.controllers;

import com.pmolinav.prediction.exceptions.CustomStatusException;
import com.pmolinav.prediction.exceptions.NotFoundException;
import com.pmolinav.prediction.services.OddsService;
import com.pmolinav.predictionslib.dto.OddsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("odds")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "5. Odds", description = "The Odds Controller. Contains all the operations that can be performed on odds.")
public class OddsController {

    @Autowired
    private OddsService oddsService;

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve odds by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<OddsDTO> findOddsById(@RequestParam String requestUid, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(oddsService.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/events/{eventType}")
    @Operation(summary = "Retrieve odds by event ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<OddsDTO>> findOddsByEventType(@RequestParam String requestUid, @PathVariable String eventType) {
        try {
            return ResponseEntity.ok(oddsService.findByEventType(eventType));
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}

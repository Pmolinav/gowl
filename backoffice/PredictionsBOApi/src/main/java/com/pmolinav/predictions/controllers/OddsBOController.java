package com.pmolinav.predictions.controllers;

import com.pmolinav.predictions.exceptions.CustomStatusException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.services.OddsBOService;
import com.pmolinav.predictionslib.dto.OddsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("odds")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "5. Odds", description = "The Odds Controller. Contains all the operations that can be performed on odds.")
public class OddsBOController {

    @Autowired
    private OddsBOService oddsBOService;

    @GetMapping
    @Operation(summary = "Retrieve all odds", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<OddsDTO>> findAllOdds(@RequestParam String requestUid) {
        try {
            return ResponseEntity.ok(oddsBOService.findAll());
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve odds by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<OddsDTO> findOddsById(@RequestParam String requestUid, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(oddsBOService.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/events/{type}")
    @Operation(summary = "Retrieve odds by event ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<OddsDTO>> findOddsByEventType(@RequestParam String requestUid, @PathVariable String type) {
        try {
            return ResponseEntity.ok(oddsBOService.findByEventType(type));
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "Get odds by match ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<OddsDTO>> findOddsByMatchId(@PathVariable Long matchId) {
        try {
            List<OddsDTO> odds = oddsBOService.findOddsByMatchId(matchId);
            return ResponseEntity.ok(odds);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping
    @Operation(summary = "Create new odds", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createOdds(@RequestParam String requestUid,
                                        @Valid @RequestBody OddsDTO oddsDTO,
                                        BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        try {
            Long createdId = oddsBOService.create(oddsDTO);
            return new ResponseEntity<>(createdId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update odds by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> updateOdds(@RequestParam String requestUid,
                                        @PathVariable Long id,
                                        @Valid @RequestBody OddsDTO oddsDTO,
                                        BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        try {
            oddsBOService.update(id, oddsDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete odds by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteOdds(@RequestParam String requestUid, @PathVariable Long id) {
        try {
            oddsBOService.delete(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/match/{matchId}")
    @Operation(summary = "Delete odds by match ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteOddsByMatchId(@RequestParam String requestUid, @PathVariable Long matchId) {
        try {
            oddsBOService.deleteByMatchId(matchId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}

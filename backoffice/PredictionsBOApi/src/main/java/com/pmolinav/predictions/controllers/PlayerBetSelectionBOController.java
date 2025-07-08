
package com.pmolinav.predictions.controllers;

import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.predictions.services.PlayerBetSelectionBOService;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
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
@RequestMapping("player-bet-selections")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "8. Player Bet Selections", description = "The Player Bet Selection Controller. Contains all operations for player bet selections.")
public class PlayerBetSelectionBOController {

    @Autowired
    private PlayerBetSelectionBOService playerBetSelectionBOService;

    @GetMapping
    @Operation(summary = "Retrieve all player bet selections", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<PlayerBetSelectionDTO>> findAllSelections(@RequestParam String requestUid) {
        try {
            return ResponseEntity.ok(playerBetSelectionBOService.findAll());
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific player bet selection by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<PlayerBetSelectionDTO> findSelectionById(@RequestParam String requestUid,
                                                                   @PathVariable Long id) {
        try {
            return ResponseEntity.ok(playerBetSelectionBOService.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/bet/{betId}")
    @Operation(summary = "Retrieve player bet selections by bet ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<PlayerBetSelectionDTO>> getSelectionsByBetId(@RequestParam String requestUid,
                                                                            @PathVariable Long betId) {
        try {
            return ResponseEntity.ok(playerBetSelectionBOService.findByBetId(betId));
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new player bet selection", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createSelection(@RequestParam String requestUid,
                                             @Valid @RequestBody PlayerBetSelectionDTO playerBetSelectionDTO,
                                             BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        try {
            Long createdId = playerBetSelectionBOService.create(playerBetSelectionDTO);
            return new ResponseEntity<>(createdId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a player bet selection by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteSelection(@RequestParam String requestUid,
                                             @PathVariable Long id) {
        try {
            playerBetSelectionBOService.delete(id);
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

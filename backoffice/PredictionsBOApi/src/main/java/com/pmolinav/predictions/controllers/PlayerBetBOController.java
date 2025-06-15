
package com.pmolinav.predictions.controllers;

import com.pmolinav.predictions.exceptions.CustomStatusException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.services.PlayerBetBOService;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
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
@RequestMapping("player-bets")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "7. Player Bets", description = "The Player Bet Controller. Contains all the operations that can be performed on player bets.")
public class PlayerBetBOController {

    @Autowired
    private PlayerBetBOService playerBetBOService;

    @GetMapping
    @Operation(summary = "Retrieve all player bets", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<PlayerBetDTO>> findAllPlayerBets(@RequestParam String requestUid) {
        try {
            return ResponseEntity.ok(playerBetBOService.findAll());
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific player bet by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<PlayerBetDTO> findPlayerBetById(@RequestParam String requestUid,
                                                          @PathVariable Long id) {
        try {
            return ResponseEntity.ok(playerBetBOService.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "Retrieve player bets by match ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<PlayerBetDTO>> getPlayerBetsByMatchId(@RequestParam String requestUid,
                                                                     @PathVariable Long matchId) {
        try {
            return ResponseEntity.ok(playerBetBOService.findByMatchId(matchId));
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Retrieve player bets by username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<PlayerBetDTO>> getPlayerBetsByUsername(@RequestParam String requestUid,
                                                                      @PathVariable String username) {
        try {
            return ResponseEntity.ok(playerBetBOService.findByUsername(username));
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new player bet", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createPlayerBet(@RequestParam String requestUid,
                                             @Valid @RequestBody PlayerBetDTO playerBetDTO,
                                             BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        try {
            Long createdId = playerBetBOService.create(playerBetDTO);
            return new ResponseEntity<>(createdId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a player bet by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deletePlayerBet(@RequestParam String requestUid,
                                             @PathVariable Long id) {
        try {
            playerBetBOService.delete(id);
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

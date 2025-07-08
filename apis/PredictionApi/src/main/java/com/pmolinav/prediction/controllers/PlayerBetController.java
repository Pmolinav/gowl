
package com.pmolinav.prediction.controllers;

import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.prediction.services.PlayerBetService;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class PlayerBetController {

    @Autowired
    private PlayerBetService playerBetService;

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific player bet by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> findPlayerBetById(@RequestParam String requestUid,
                                               @PathVariable Long id) {
        try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            PlayerBetDTO playerBetDTO = playerBetService.findById(id);

            if (!playerBetDTO.getUsername().equals(username)) {
                return new ResponseEntity<>("Username in request does not match authenticated user", HttpStatus.FORBIDDEN);
            }
            return ResponseEntity.ok(playerBetDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "Retrieve player bets by match ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> findPlayerBetsByMatchId(@RequestParam String requestUid,
                                                     @PathVariable Long matchId) {
        try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<PlayerBetDTO> playerBetListDTO = playerBetService.findByMatchId(matchId);
            if (playerBetListDTO.stream().noneMatch(playerBetDTO ->
                    playerBetDTO.getUsername().equals(username))) {
                return new ResponseEntity<>("Username in request does not match authenticated user", HttpStatus.FORBIDDEN);
            }
            return ResponseEntity.ok(playerBetListDTO);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Retrieve player bets by username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> findPlayerBetsByUsername(@RequestParam String requestUid,
                                                      @PathVariable String username) {
        try {
            String authUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (!username.equals(authUsername)) {
                return new ResponseEntity<>("Username in request does not match authenticated user", HttpStatus.FORBIDDEN);
            }
            return ResponseEntity.ok(playerBetService.findByUsername(username));
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new player bet", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createPlayerBet(@RequestParam String requestUid,
                                             @Valid @RequestBody PlayerBetDTO playerBetDTO,
                                             BindingResult result) {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!playerBetDTO.getUsername().equals(username)) {
            return new ResponseEntity<>("Username in request does not match authenticated user", HttpStatus.FORBIDDEN);
        }

        if (result.hasErrors()) {
            return validation(result);
        }

        try {
            Long createdId = playerBetService.create(playerBetDTO);
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
            playerBetService.delete(id);
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

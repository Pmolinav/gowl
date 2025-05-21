
package com.pmolinav.leagues.controllers;

import com.pmolinav.leagues.exceptions.CustomStatusException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.services.LeaguePlayerPointsBOService;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("league-players-points")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "6. League player points", description = "The league player points Controller. Contains all the operations that can be performed on league player points.")
public class LeaguePlayerPointsBOController {

    @Autowired
    private final LeaguePlayerPointsBOService leaguePlayerPointsBOService;

    @Autowired
    public LeaguePlayerPointsBOController(LeaguePlayerPointsBOService leaguePlayerPointsBOService) {
        this.leaguePlayerPointsBOService = leaguePlayerPointsBOService;
    }

    @GetMapping("/leagues/{id}/players/{username}")
    @Operation(summary = "Retrieve league player info by league ID and username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeaguePlayerPointsDTO>> findLeaguePlayerPointsByLeagueIdAndPlayer(@RequestParam String requestUid,
                                                                                                 @PathVariable Long id,
                                                                                                 @PathVariable String username) {
        try {
            List<LeaguePlayerPointsDTO> leaguePlayerPoints =
                    leaguePlayerPointsBOService.findLeaguePlayerPointsByLeagueIdAndPlayer(id, username);
            return ResponseEntity.ok(leaguePlayerPoints);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    @Operation(summary = "Retrieve league player info by league ID and username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeaguePlayerPointsDTO>> findLeaguePlayerPointsByCategorySeasonAndNumber(@RequestParam String requestUid,
                                                                                                       @PathVariable String categoryId,
                                                                                                       @PathVariable Integer season,
                                                                                                       @PathVariable Integer number) {
        try {
            List<LeaguePlayerPointsDTO> leaguePlayerPoints =
                    leaguePlayerPointsBOService.findLeaguePlayerPointsByCategorySeasonAndNumber(categoryId, season, number);
            return ResponseEntity.ok(leaguePlayerPoints);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping
    @Operation(summary = "Retrieve league player info by league ID and username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createLeaguePlayersPoints(@RequestParam String requestUid,
                                                       @Valid @RequestBody LeaguePlayerPointsDTO leaguePlayerPoints,
                                                       BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            LeaguePlayerPointsDTO createdLeaguePlayerPoints = leaguePlayerPointsBOService.createLeaguePlayerPoints(leaguePlayerPoints);

            return new ResponseEntity<>(createdLeaguePlayerPoints, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }

    }

    @DeleteMapping("/leagues/{id}/players/{username}")
    @Operation(summary = "Retrieve league player info by league ID and username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteLeaguePlayerPointsByLeagueIdAndPlayer(@RequestParam String requestUid,
                                                                         @PathVariable Long id,
                                                                         @PathVariable String username) {
        try {
            leaguePlayerPointsBOService.deleteLeaguePlayerPointsByLeagueIdAndPlayer(id, username);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    @Operation(summary = "Retrieve league player info by league ID and username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteLeaguePlayerByCategorySeasonAndNumber(@RequestParam String requestUid,
                                                                         @PathVariable String categoryId,
                                                                         @PathVariable Integer season,
                                                                         @PathVariable Integer number) {
        try {
            leaguePlayerPointsBOService.deleteLeaguePlayerByCategorySeasonAndNumber(categoryId, season, number);

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

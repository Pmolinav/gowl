
package com.pmolinav.leagues.controllers;

import com.pmolinav.leagues.exceptions.CustomStatusException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.services.LeaguePlayersBOService;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("league-players")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "5. League players", description = "The league players Controller. Contains all the operations that can be performed on league players.")
public class LeaguePlayerBOController {

    @Autowired
    private final LeaguePlayersBOService leaguePlayersBOService;

    @Autowired
    public LeaguePlayerBOController(LeaguePlayersBOService leaguePlayersBOService) {
        this.leaguePlayersBOService = leaguePlayersBOService;
    }

    @GetMapping("/leagues/{id}/players/{username}")
    @Operation(summary = "Retrieve league player info by league ID and username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<LeaguePlayerDTO> findLeaguePlayerByLeagueIdAndPlayer(@RequestParam String requestUid,
                                                                               @PathVariable Long id,
                                                                               @PathVariable String username) {
        try {
            LeaguePlayerDTO leaguePlayer = leaguePlayersBOService.findLeaguePlayerByByLeagueIdAndPlayer(id, username);

            return ResponseEntity.ok(leaguePlayer);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/leagues/{id}")
    @Operation(summary = "Retrieve the players and their info by league ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeaguePlayerDTO>> findLeaguePlayersByUsername(@RequestParam String requestUid,
                                                                             @PathVariable Long id) {
        try {
            List<LeaguePlayerDTO> leagues = leaguePlayersBOService.findLeaguePlayersByLeagueId(id);

            return ResponseEntity.ok(leagues);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/players/{username}/leagues")
    @Operation(summary = "Retrieve the leagues for a player by username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeagueDTO>> findLeaguePlayersByLeagueId(@RequestParam String requestUid,
                                                                       @PathVariable String username) {
        try {
            List<LeagueDTO> leagues = leaguePlayersBOService.findLeaguesByUsername(username);

            return ResponseEntity.ok(leagues);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping
    @Operation(summary = "Create league players", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createLeaguePlayers(@RequestParam String requestUid,
                                                 @RequestBody List<LeaguePlayerDTO> leaguePlayers,
                                                 BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            if (CollectionUtils.isEmpty(leaguePlayers)) {
                return ResponseEntity.badRequest().build();
            }
            List<LeaguePlayerId> createdLeaguePlayers = leaguePlayersBOService.createLeaguePlayers(leaguePlayers);

            return new ResponseEntity<>(createdLeaguePlayers, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }

    }

    @PutMapping("/leagues/{id}/players/{username}")
    @Operation(summary = "Add points to a player in their league", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> addPointsToLeaguePlayer(@RequestParam String requestUid,
                                                     @PathVariable Long id,
                                                     @PathVariable String username,
                                                     @RequestParam Integer points) {
        try {
            leaguePlayersBOService.addPointsToLeaguePlayer(id, username, points);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/leagues/{id}")
    @Operation(summary = "Delete all the players of a league", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteLeaguePlayersByLeagueId(@RequestParam String requestUid,
                                                           @PathVariable Long id) {
        try {
            leaguePlayersBOService.deleteLeaguePlayersByLeagueId(id);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/leagues/{id}/players/{username}")
    @Operation(summary = "Delete a player by username from a league ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteLeaguePlayersByLeagueIdAndPlayer(@RequestParam String requestUid,
                                                                    @PathVariable Long id,
                                                                    @PathVariable String username) {
        try {
            leaguePlayersBOService.deleteLeaguePlayersByLeagueIdAndPlayer(id, username);

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

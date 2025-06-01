
package com.pmolinav.league.controllers;

import com.pmolinav.league.exceptions.CustomStatusException;
import com.pmolinav.league.exceptions.NotFoundException;
import com.pmolinav.league.services.LeaguePlayersService;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class LeaguePlayerController {

    @Autowired
    private final LeaguePlayersService leaguePlayersService;

    @Autowired
    public LeaguePlayerController(LeaguePlayersService leaguePlayersService) {
        this.leaguePlayersService = leaguePlayersService;
    }

    @GetMapping("/leagues/{id}/players/{username}")
    @Operation(summary = "Retrieve league player info by league ID and username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<LeaguePlayerDTO> findLeaguePlayerByLeagueIdAndPlayer(@RequestParam String requestUid,
                                                                               @PathVariable Long id,
                                                                               @PathVariable String username) {
        try {
            LeaguePlayerDTO leaguePlayer = leaguePlayersService.findLeaguePlayerByByLeagueIdAndPlayer(id, username);

            return ResponseEntity.ok(leaguePlayer);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/leagues/{id}")
    @Operation(summary = "Retrieve the players and their info by league ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeaguePlayerDTO>> findLeaguePlayersByLeagueId(@RequestParam String requestUid,
                                                                             @PathVariable Long id) {
        try {
            List<LeaguePlayerDTO> leagues = leaguePlayersService.findLeaguePlayersByLeagueId(id);

            return ResponseEntity.ok(leagues);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/players/{username}/leagues")
    @Operation(summary = "Retrieve the leagues for a player by username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeagueDTO>> findLeaguePlayersByUsername(@RequestParam String requestUid,
                                                                       @PathVariable String username) {
        try {
            List<LeagueDTO> leagues = leaguePlayersService.findLeaguesByUsername(username);

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
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (leaguePlayers.size() != 1 || !leaguePlayers.getFirst().getUsername().equals(username)) {
                return new ResponseEntity<>("Username in request does not match authenticated user", HttpStatus.FORBIDDEN);
            }
            if (result.hasErrors()) {
                return validation(result);
            }
            if (CollectionUtils.isEmpty(leaguePlayers)) {
                return ResponseEntity.badRequest().build();
            }
            List<LeaguePlayerId> createdLeaguePlayers = leaguePlayersService.createLeaguePlayers(leaguePlayers);

            return new ResponseEntity<>(createdLeaguePlayers, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }

    }

    // TODO: Change by update League PlayerStatus?
    @DeleteMapping("/leagues/{id}/players/{username}")
    @Operation(summary = "Delete a player by username from a league ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteLeaguePlayersByLeagueIdAndPlayer(@RequestParam String requestUid,
                                                                    @PathVariable Long id,
                                                                    @PathVariable String username) {
        try {
            leaguePlayersService.deleteLeaguePlayersByLeagueIdAndPlayer(id, username);

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

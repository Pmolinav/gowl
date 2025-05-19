
package com.pmolinav.leagues.controllers;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.services.LeaguePlayerService;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("league-players")
public class LeaguePlayerController {

    @Autowired
    private final LeaguePlayerService leaguePlayerService;

    @Autowired
    public LeaguePlayerController(LeaguePlayerService leaguePlayerService) {
        this.leaguePlayerService = leaguePlayerService;
    }

    @GetMapping("/leagues/{id}")
    public ResponseEntity<List<LeaguePlayerDTO>> findLeaguePlayersByLeagueId(@PathVariable Long id) {
        try {
            List<LeaguePlayerDTO> leaguePlayers = leaguePlayerService.findLeaguePlayersByLeagueId(id);
            return ResponseEntity.ok(leaguePlayers);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/leagues/{id}/players/{username}")
    public ResponseEntity<LeaguePlayerDTO> findLeaguePlayerByLeagueIdAndPlayer(@PathVariable Long id,
                                                                               @PathVariable String username) {
        try {
            LeaguePlayerDTO leaguePlayer = leaguePlayerService.findLeaguePlayerByLeagueIdAndPlayer(id, username);
            return ResponseEntity.ok(leaguePlayer);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/players/{username}/leagues")
    public ResponseEntity<List<LeagueDTO>> findLeaguePlayersByUsername(@PathVariable String username) {
        try {
            List<LeagueDTO> leagues = leaguePlayerService.findLeaguesByUsername(username);
            return ResponseEntity.ok(leagues);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<List<LeaguePlayerId>> createLeaguePlayers(@RequestBody List<LeaguePlayerDTO> leaguePlayers) {
        try {
            if (CollectionUtils.isEmpty(leaguePlayers)) {
                return ResponseEntity.badRequest().build();
            }
            List<LeaguePlayerId> createdLeaguePlayers = leaguePlayerService.createLeaguePlayers(leaguePlayers);

//            leaguesService.storeInKafka(ChangeType.CREATE, createdLeague.getLeagueId(), createdLeague);

            return new ResponseEntity<>(createdLeaguePlayers, HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @PutMapping("/leagues/{id}/players/{username}")
    public ResponseEntity<?> addPointsToLeaguePlayer(@PathVariable Long id,
                                                     @PathVariable String username,
                                                     @RequestParam Integer points) {
        try {
            leaguePlayerService.addPointsToLeaguePlayer(id, username, points);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/leagues/{id}")
    public ResponseEntity<?> deleteLeaguePlayersByLeagueId(@PathVariable Long id) {
        try {
            leaguePlayerService.deleteLeaguePlayersByLeagueId(id);

//            leagueService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/leagues/{id}/players/{username}")
    public ResponseEntity<?> deleteLeaguePlayersByLeagueIdAndPlayer(@PathVariable Long id,
                                                                    @PathVariable String username) {
        try {
            leaguePlayerService.deleteLeaguePlayersByLeagueIdAndUsername(id, username);

//            leagueService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}


package com.pmolinav.leagues.controllers;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagues.services.LeaguePlayerPointsService;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("league-player-points")
public class LeaguePlayerPointsController {

    @Autowired
    private final LeaguePlayerPointsService leaguePlayerPointsService;

    @Autowired
    public LeaguePlayerPointsController(LeaguePlayerPointsService leaguePlayerPointsService) {
        this.leaguePlayerPointsService = leaguePlayerPointsService;
    }

    @GetMapping("/leagues/{id}/players/{username}")
    public ResponseEntity<List<LeaguePlayerPointsDTO>> findLeaguePlayerPointsByLeagueIdAndPlayer(@PathVariable Long id,
                                                                                                 @PathVariable String username) {
        try {
            List<LeaguePlayerPointsDTO> leaguePlayerPoints =
                    leaguePlayerPointsService.findByLeagueIdAndPlayer(id, username);
            return ResponseEntity.ok(leaguePlayerPoints);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    public ResponseEntity<List<LeaguePlayerPointsDTO>> findLeaguePlayerPointsByCategorySeasonAndNumber(@PathVariable String categoryId,
                                                                                                       @PathVariable Integer season,
                                                                                                       @PathVariable Integer number) {
        try {
            List<LeaguePlayerPointsDTO> leaguePlayerPoints =
                    leaguePlayerPointsService.findByCategoryIdSeasonAndNumber(categoryId, season, number);
            return ResponseEntity.ok(leaguePlayerPoints);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<LeaguePlayerPointsDTO> createOrUpdateLeaguePlayersPoints(@RequestBody LeaguePlayerPointsDTO leaguePlayerPoints) {
        try {
            LeaguePlayerPointsDTO createdLeaguePlayerPoints = leaguePlayerPointsService.createOrUpdateLeaguePlayerPoints(leaguePlayerPoints);

            return new ResponseEntity<>(createdLeaguePlayerPoints, HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @DeleteMapping("/leagues/{id}/players/{username}")
    public ResponseEntity<?> deleteLeaguePlayerPointsByLeagueIdAndPlayer(@PathVariable Long id,
                                                                         @PathVariable String username) {
        try {
            leaguePlayerPointsService.deleteLeaguePlayerPointsByLeagueIdAndUsername(id, username);

//            leagueService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    public ResponseEntity<?> deleteLeaguePlayerByCategorySeasonAndNumber(@PathVariable String categoryId,
                                                                         @PathVariable Integer season,
                                                                         @PathVariable Integer number) {
        try {
            leaguePlayerPointsService.deleteLeaguePlayerPointsByCategoryIdSeasonAndNumber(categoryId, season, number);

//            leagueService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}

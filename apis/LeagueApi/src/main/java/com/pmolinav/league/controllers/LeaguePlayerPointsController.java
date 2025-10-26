
package com.pmolinav.league.controllers;

import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.league.exceptions.CustomStatusException;
import com.pmolinav.league.exceptions.NotFoundException;
import com.pmolinav.league.services.LeaguePlayerPointsService;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("league-player-points")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "6. League player points", description = "The league player points Controller. Contains all the operations that can be performed on league player points.")
public class LeaguePlayerPointsController {

    private static final Logger logger = LoggerFactory.getLogger(LeaguePlayerPointsController.class);

    @Autowired
    private final LeaguePlayerPointsService leaguePlayerPointsService;

    @Autowired
    public LeaguePlayerPointsController(LeaguePlayerPointsService leaguePlayerPointsService) {
        this.leaguePlayerPointsService = leaguePlayerPointsService;
    }

    @GetMapping("/leagues/{id}/players/{username}")
    @Operation(summary = "Retrieve league player info by league ID and username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeaguePlayerPointsDTO>> findLeaguePlayerPointsByLeagueIdAndPlayer(@RequestParam String requestUid,
                                                                                                 @PathVariable Long id,
                                                                                                 @PathVariable String username) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            MDC.put(MDCCommonKeys.LEAGUE_ID.key(), String.valueOf(id));
            MDC.put(MDCCommonKeys.USERNAME.key(), username);
            logger.info("LeaguePlayerPointsController: findLeaguePlayerPointsByLeagueIdAndPlayer. Path: id: {}, username: {}", id, username);
            List<LeaguePlayerPointsDTO> leaguePlayerPoints =
                    leaguePlayerPointsService.findLeaguePlayerPointsByLeagueIdAndPlayer(id, username);
            return ResponseEntity.ok(leaguePlayerPoints);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
            MDC.remove(MDCCommonKeys.LEAGUE_ID.key());
            MDC.remove(MDCCommonKeys.USERNAME.key());
        }
    }

    //TODO: Este me va a sobrar yo creo.
    @GetMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    @Operation(summary = "Retrieve league player info by category ID, season and number", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeaguePlayerPointsDTO>> findLeaguePlayerPointsByCategorySeasonAndNumber(@RequestParam String requestUid,
                                                                                                       @PathVariable String categoryId,
                                                                                                       @PathVariable Integer season,
                                                                                                       @PathVariable Integer number) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("LeaguePlayerPointsController: findLeaguePlayerPointsByCategorySeasonAndNumber. " +
                    "Path: categoryId: {}, season: {}, number: {}", categoryId, season, number);
            List<LeaguePlayerPointsDTO> leaguePlayerPoints =
                    leaguePlayerPointsService.findLeaguePlayerPointsByCategorySeasonAndNumber(categoryId, season, number);
            return ResponseEntity.ok(leaguePlayerPoints);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }
}

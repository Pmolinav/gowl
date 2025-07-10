package com.pmolinav.prediction.clients;

import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.dto.OddsDTO;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "predictionsservice")
public interface PredictionsServiceClient {

    // HEALTH
    @GetMapping("/actuator/health")
    void health();

    // MATCHES
    @GetMapping("/matches/{id}")
    MatchDTO findMatchById(@PathVariable("id") Long id);

    @GetMapping("/matches/categories/{categoryId}/seasons/{season}/number/{number}")
    List<MatchDTO> findByCategoryIdSeasonAndMatchDayNumber(
            @PathVariable String categoryId,
            @PathVariable Integer season,
            @PathVariable Integer number);

    // EVENTS
    @GetMapping("/events/{type}")
    EventDTO findByEventType(@PathVariable("type") String type);

    // ODDS
    @GetMapping("/odds/{id}")
    OddsDTO findOddsById(@PathVariable("id") Long id);

    @GetMapping("/odds/events/{type}")
    List<OddsDTO> findOddsByEventType(@PathVariable("type") String type);

    @GetMapping("/odds/match/{matchId}")
    List<OddsDTO> findOddsByMatchId(@PathVariable("matchId") Long matchId);

    // PLAYER BETS
    @GetMapping("/player-bets/{id}")
    PlayerBetDTO findPlayerBetById(@PathVariable("id") Long id);

    @GetMapping("/player-bets/match/{matchId}")
    List<PlayerBetDTO> findPlayerBetsByMatchId(@PathVariable("matchId") Long matchId);

    @GetMapping("/player-bets/username/{username}")
    List<PlayerBetDTO> findPlayerBetsByUsername(@PathVariable("username") String username);

    @PostMapping("/player-bets")
    Long create(@RequestBody PlayerBetDTO betDTO);

    @DeleteMapping("/player-bets/{id}")
    void delete(@PathVariable("id") Long id);
}
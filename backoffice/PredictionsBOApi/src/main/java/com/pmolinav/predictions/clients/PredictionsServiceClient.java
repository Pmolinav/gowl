package com.pmolinav.predictions.clients;

import com.pmolinav.predictionslib.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "predictionsservice")
public interface PredictionsServiceClient {

    // HEALTH
    @GetMapping("/actuator/health")
    void health();

    // MATCHES
    @GetMapping("/matches")
    List<MatchDTO> findAllMatches();

    @GetMapping("/matches/{id}")
    MatchDTO findMatchById(@PathVariable("id") Long id);

    @GetMapping("/matches/categories/{categoryId}/seasons/{season}/number/{number}")
    List<MatchDTO> findMatchesByCategoryIdSeasonAndMatchDayNumber(
            @PathVariable String categoryId,
            @PathVariable Integer season,
            @PathVariable Integer number);

    @PostMapping("/matches")
    Long createMatch(@RequestBody MatchDTO matchDTO);

    @PutMapping("/matches/{id}")
    void updateMatch(@PathVariable("id") Long id, @RequestBody MatchDTO matchDTO);

    @DeleteMapping("/matches/{id}")
    void deleteMatch(@PathVariable("id") Long id);

    // EVENTS
    @GetMapping("/events")
    List<EventDTO> findAll();

    @GetMapping("/events/{type}")
    EventDTO findByType(@PathVariable("type") String type);

    @PostMapping("/events")
    Long create(@RequestBody EventDTO eventDTO);

    @PutMapping("/events/{type}")
    void update(@PathVariable("type") String type, @RequestBody EventDTO eventDTO);

    @DeleteMapping("/events/{type}")
    void delete(@PathVariable("type") String type);

    // ODDS
    @GetMapping("/odds")
    List<OddsDTO> findAllOdds();

    @GetMapping("/odds/{id}")
    OddsDTO findOddsById(@PathVariable("id") Long id);

    @GetMapping("/odds/events/{type}")
    List<OddsDTO> findOddsByEventType(@PathVariable("type") String type);

    @GetMapping("/odds/match/{matchId}")
    List<OddsDTO> findOddsByMatchId(@PathVariable("matchId") Long matchId);

    @PostMapping("/odds")
    Long createOdds(@RequestBody OddsDTO oddsDTO);

    @PutMapping("/odds/{id}")
    void updateOdds(@PathVariable("id") Long id, @RequestBody OddsDTO oddsDTO);

    @DeleteMapping("/odds/{id}")
    void deleteOdds(@PathVariable("id") Long id);

    @DeleteMapping("/odds/match/{matchId}")
    void deleteOddsByMatchId(@PathVariable("matchId") Long matchId);

    // PLAYER BETS
    @GetMapping("/player-bets")
    List<PlayerBetDTO> findAllPlayerBets();

    @GetMapping("/player-bets/{id}")
    PlayerBetDTO findPlayerBetById(@PathVariable("id") Long id);

    @GetMapping("/player-bets/match/{matchId}")
    List<PlayerBetDTO> findPlayerBetByMatchId(@PathVariable("matchId") Long matchId);

    @GetMapping("/player-bets/username/{username}")
    List<PlayerBetDTO> findPlayerBetByUsername(@PathVariable("username") String username);

    @PostMapping("/player-bets")
    Long createPlayerBet(@RequestBody PlayerBetDTO betDTO);

    @DeleteMapping("/player-bets/{id}")
    void deletePlayerBet(@PathVariable("id") Long id);

    // PLAYER BET SELECTIONS
    @GetMapping("/player-bet-selections")
    List<PlayerBetSelectionDTO> findAllPlayerBetSelections();

    @GetMapping("/player-bet-selections/{id}")
    PlayerBetSelectionDTO findPlayerBetSelectionById(@PathVariable("id") Long id);

    @GetMapping("/player-bet-selections/bet/{betId}")
    List<PlayerBetSelectionDTO> findPlayerBetSelectionsByBetId(@PathVariable("betId") Long betId);

    @PostMapping("/player-bet-selections")
    Long createPlayerBetSelection(@RequestBody PlayerBetSelectionDTO selectionDTO);

    @DeleteMapping("/player-bet-selections/{id}")
    void deletePlayerBetSelection(@PathVariable("id") Long id);
}
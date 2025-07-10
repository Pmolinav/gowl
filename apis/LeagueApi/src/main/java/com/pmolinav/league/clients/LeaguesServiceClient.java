package com.pmolinav.league.clients;

import com.pmolinav.leagueslib.dto.*;
import com.pmolinav.leagueslib.model.LeagueCategory;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import com.pmolinav.leagueslib.model.MatchDayId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "leaguesservice")
public interface LeaguesServiceClient {

    // HEALTH
    @GetMapping("/actuator/health")
    void health();

    // CATEGORIES
    @GetMapping("/categories")
    List<LeagueCategory> findAllLeagueCategories();

    @GetMapping("/categories/{id}")
    LeagueCategory findLeagueCategoryById(@PathVariable String id);

    @PostMapping("/categories")
    String createLeagueCategory(@RequestBody LeagueCategoryDTO leagueCategoryDTO);

    @DeleteMapping("/categories/{id}")
    void deleteLeagueCategory(@PathVariable String id);

    // MATCH DAYS
    @GetMapping("/match-days")
    List<MatchDayDTO> findAllMatchDays();

    @GetMapping("/match-days/categories/{categoryId}")
    List<MatchDayDTO> findMatchDayByCategoryId(@PathVariable String categoryId);

    @GetMapping("/match-days/categories/{categoryId}/seasons/{season}")
    List<MatchDayDTO> findMatchDayByCategoryIdAndSeason(@PathVariable String categoryId, @PathVariable int season);

    @PostMapping("/match-days")
    MatchDayId createMatchDay(@RequestBody MatchDayDTO matchDayDTO);

    @PostMapping("/match-days/bulk")
    List<MatchDayId> createMatchDays(@RequestBody List<MatchDayDTO> matchDayDTOList);

    @DeleteMapping("/match-days/categories/{categoryId}")
    void deleteMatchDaysByCategoryId(@PathVariable String categoryId);

    @DeleteMapping("/match-days/categories/{categoryId}/seasons/{season}")
    void deleteMatchDaysByCategoryIdAndSeason(@PathVariable String categoryId, @PathVariable int season);

    @DeleteMapping("/match-days/categories/{categoryId}/seasons/{season}/number/{number}")
    void deleteMatchDayByCategoryIdSeasonAndNumber(@PathVariable String categoryId, @PathVariable int season, @PathVariable int number);

    // LEAGUES
    @GetMapping("/leagues")
    List<LeagueDTO> findAllLeagues();

    @GetMapping("/leagues/{id}")
    LeagueDTO findLeagueById(@PathVariable long id);

    @GetMapping("/leagues/names/{name}")
    LeagueDTO findLeagueByName(@PathVariable String name);

    @PostMapping("/leagues")
    Long createLeague(@RequestBody LeagueDTO leagueDTO);

    @PutMapping("/leagues/close/{id}")
    void closeLeagueById(@PathVariable Long id);

    @PutMapping("/leagues/close/names/{name}")
    void closeLeagueByName(@PathVariable String name);

    @DeleteMapping("/leagues/{id}")
    void deleteLeague(@PathVariable long id);

    @DeleteMapping("/leagues/names/{name}")
    void deleteLeagueByName(@PathVariable String name);

    // LEAGUE PLAYERS
    @GetMapping("/league-players/leagues/{id}")
    List<LeaguePlayerDTO> findLeaguePlayersByLeagueId(@PathVariable long id);

    @GetMapping("/league-players/leagues/{id}/players/{username}")
    LeaguePlayerDTO findLeaguePlayerByLeagueIdAndPlayer(@PathVariable long id, @PathVariable String username);

    @GetMapping("/league-players/players/{username}/leagues")
    List<LeagueDTO> findLeaguesByUsername(@PathVariable String username);

    @PostMapping("/league-players")
    List<LeaguePlayerId> createLeaguePlayers(@RequestBody List<LeaguePlayerDTO> leaguePlayers);

    @PutMapping("/league-players/leagues/{id}/players/{username}")
    void addPointsToLeaguePlayer(@PathVariable long id, @PathVariable String username, @RequestParam int points);

    @DeleteMapping("/league-players/leagues/{id}")
    void deleteLeaguePlayersByLeagueId(@PathVariable long id);

    @DeleteMapping("/league-players/leagues/{id}/players/{username}")
    void deleteLeaguePlayersByLeagueIdAndPlayer(@PathVariable long id, @PathVariable String username);

    // LEAGUE PLAYER POINTS
    @GetMapping("/league-player-points/leagues/{id}/players/{username}")
    List<LeaguePlayerPointsDTO> findLeaguePlayerPointsByLeagueIdAndPlayer(@PathVariable long id, @PathVariable String username);

    @GetMapping("/league-player-points/categories/{categoryId}/seasons/{season}/number/{number}")
    List<LeaguePlayerPointsDTO> findLeaguePlayerPointsByCategorySeasonAndNumber(@PathVariable String categoryId, @PathVariable int season, @PathVariable int number);

    @PostMapping("/league-player-points")
    LeaguePlayerPointsDTO createOrUpdateLeaguePlayerPoints(@RequestBody LeaguePlayerPointsDTO leaguePlayerPoints);

    @DeleteMapping("/league-player-points/leagues/{id}/players/{username}")
    void deleteLeaguePlayerPointsByLeagueIdAndPlayer(@PathVariable long id, @PathVariable String username);

    @DeleteMapping("/league-player-points/categories/{categoryId}/seasons/{season}/number/{number}")
    void deleteLeaguePlayerByCategorySeasonAndNumber(@PathVariable String categoryId, @PathVariable int season, @PathVariable int number);

}

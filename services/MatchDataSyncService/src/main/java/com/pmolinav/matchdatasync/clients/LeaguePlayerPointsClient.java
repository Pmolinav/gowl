package com.pmolinav.matchdatasync.clients;

import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "LeaguePlayerPointsClient", url = "leaguesservice:8004/league-player-points")
public interface LeaguePlayerPointsClient {

    @GetMapping("/leagues/{id}/players/{username}")
    List<LeaguePlayerPointsDTO> findLeaguePlayerPointsByLeagueIdAndPlayer(@PathVariable long id, @PathVariable String username);

    @GetMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    List<LeaguePlayerPointsDTO> findLeaguePlayerPointsByCategorySeasonAndNumber(@PathVariable String categoryId, @PathVariable int season, @PathVariable int number);

    @PostMapping
    LeaguePlayerPointsDTO createOrUpdateLeaguePlayerPoints(@RequestBody LeaguePlayerPointsDTO leaguePlayerPoints);

    @DeleteMapping("/leagues/{id}/players/{username}")
    void deleteLeaguePlayerPointsByLeagueIdAndPlayer(@PathVariable long id, @PathVariable String username);

    @DeleteMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    void deleteLeaguePlayerByCategorySeasonAndNumber(@PathVariable String categoryId, @PathVariable int season, @PathVariable int number);
}

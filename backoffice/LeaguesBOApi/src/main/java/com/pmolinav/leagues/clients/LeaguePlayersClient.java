package com.pmolinav.leagues.clients;

import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "LeaguePlayersClient", url = "leaguesservice:8004/league-players")
public interface LeaguePlayersClient {

    @GetMapping("/leagues/{id}")
    List<LeaguePlayerDTO> findLeaguePlayersByLeagueId(@PathVariable long id);

    @GetMapping("/leagues/{id}/players/{username}")
    LeaguePlayerDTO findLeaguePlayerByLeagueIdAndPlayer(@PathVariable long id, @PathVariable String username);

    @GetMapping("/players/{username}/leagues")
    List<LeagueDTO> findLeaguePlayersByUsername(@PathVariable String username);

    @PostMapping
    List<LeaguePlayerId> createLeaguePlayers(@RequestBody List<LeaguePlayerDTO> leaguePlayers);

    @PutMapping("/leagues/{id}/players/{username}")
    void addPointsToLeaguePlayer(@PathVariable long id, @PathVariable String username, @RequestParam int points);

    @DeleteMapping("/leagues/{id}")
    void deleteLeaguePlayersByLeagueId(@PathVariable long id);

    @DeleteMapping("/leagues/{id}/players/{username}")
    void deleteLeaguePlayersByLeagueIdAndPlayer(@PathVariable long id, @PathVariable String name);
}

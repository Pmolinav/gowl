package com.pmolinav.leagues.clients;

import com.pmolinav.leagueslib.dto.LeagueDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "LeaguesClient", url = "leaguesservice:8004/leagues")
public interface LeaguesClient {

    @GetMapping
    List<LeagueDTO> findAllLeagues();

    @GetMapping("/{id}")
    LeagueDTO findLeagueById(@PathVariable long id);

    @GetMapping("/names/{name}")
    LeagueDTO findLeagueByName(@PathVariable String name);

    @PostMapping
    Long createLeague(@RequestBody LeagueDTO leagueDTO);

    @DeleteMapping("/{id}")
    void deleteLeague(@PathVariable long id);

    @DeleteMapping("/names/{name}")
    void deleteLeagueByName(@PathVariable String name);
}

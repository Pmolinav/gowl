package com.pmolinav.predictions.clients;

import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "PlayerBetClient", url = "http://predictionsservice:8007", path = "/player-bets")
public interface PlayerBetClient {

    @GetMapping
    List<PlayerBetDTO> findAll();

    @GetMapping("/{id}")
    PlayerBetDTO findById(@PathVariable("id") Long id);

    @GetMapping("/match/{matchId}")
    List<PlayerBetDTO> findByMatchId(@PathVariable("matchId") Long matchId);

    @GetMapping("/username/{username}")
    List<PlayerBetDTO> findByUsername(@PathVariable("username") String username);

    @PostMapping
    Long create(@RequestBody PlayerBetDTO betDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);
}
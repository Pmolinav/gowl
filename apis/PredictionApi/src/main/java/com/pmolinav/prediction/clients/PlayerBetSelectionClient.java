package com.pmolinav.prediction.clients;

import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "PlayerBetSelectionClient", url = "http://predictionsservice:8007", path = "/player-bet-selections")
public interface PlayerBetSelectionClient {

    @GetMapping
    List<PlayerBetSelectionDTO> findAll();

    @GetMapping("/{id}")
    PlayerBetSelectionDTO findById(@PathVariable("id") Long id);

    @GetMapping("/bet/{betId}")
    List<PlayerBetSelectionDTO> findByBetId(@PathVariable("betId") Long betId);

    @PostMapping
    Long create(@RequestBody PlayerBetSelectionDTO selectionDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);
}
package com.pmolinav.predictions.clients;

import com.pmolinav.predictionslib.dto.MatchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "MatchClient", url = "http://predictionsservice:8007", path = "/matches")
public interface MatchClient {

    @GetMapping
    List<MatchDTO> findAll();

    @GetMapping("/{id}")
    MatchDTO findById(@PathVariable("id") Long id);

    @GetMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    List<MatchDTO> findByCategoryIdSeasonAndMatchDayNumber(
            @PathVariable String categoryId,
            @PathVariable Integer season,
            @PathVariable Integer number);

    @PostMapping
    Long create(@RequestBody MatchDTO matchDTO);

    @PutMapping("/{id}")
    void update(@PathVariable("id") Long id, @RequestBody MatchDTO matchDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);
}
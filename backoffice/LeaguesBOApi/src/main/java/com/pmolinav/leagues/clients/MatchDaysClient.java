package com.pmolinav.leagues.clients;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.model.MatchDayId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "MatchDaysClient", url = "leaguesservice:8004/match-days")
public interface MatchDaysClient {

    @GetMapping
    List<MatchDayDTO> findAllMatchDays();

    @GetMapping("/categories/{categoryId}")
    List<MatchDayDTO> findMatchDayByCategoryId(@PathVariable String categoryId);

    @GetMapping("/categories/{categoryId}/seasons/{season}")
    List<MatchDayDTO> findMatchDayByCategoryIdAndSeason(@PathVariable String categoryId, @PathVariable int season);

    @PostMapping
    MatchDayId createMatchDay(@RequestBody MatchDayDTO matchDayDTO);

    @DeleteMapping("/categories/{categoryId}")
    void deleteMatchDaysByCategoryId(@PathVariable String categoryId);

    @DeleteMapping("/categories/{categoryId}/seasons/{season}")
    void deleteMatchDayByCategoryIdAndSeason(@PathVariable String categoryId, @PathVariable int season);

    @DeleteMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    void deleteMatchDayByCategoryIdSeasonAndNumber(@PathVariable String categoryId, @PathVariable int season, @PathVariable int number);
}

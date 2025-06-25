package com.pmolinav.matchdatasync.clients;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "MatchDaysClient", url = "leaguesservice:8004/match-days")
public interface MatchDaysClient {

    @GetMapping
    List<MatchDayDTO> findAllMatchDays(@RequestParam(value = "dateFrom", required = false) Long dateFrom,
                                       @RequestParam(value = "dateTo", required = false) Long dateTo,
                                       @RequestParam(value = "synced", required = false) Boolean synced);

    @GetMapping("/completed")
    List<MatchDayDTO> findCompletedMatchDays(@RequestParam(name = "endDateFrom", required = false) Long endDateFrom,
                                             @RequestParam(name = "endDateTo", required = false) Long endDateTo,
                                             @RequestParam(name = "resultsChecked", required = false) Boolean resultsChecked);

    @PutMapping
    void updateMatchDay(@RequestBody MatchDayDTO matchDayDTO);
}

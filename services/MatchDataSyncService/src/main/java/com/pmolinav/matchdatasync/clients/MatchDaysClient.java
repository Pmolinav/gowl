package com.pmolinav.matchdatasync.clients;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.model.MatchDayId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "MatchDaysClient", url = "leaguesservice:8004/match-days")
public interface MatchDaysClient {

    @GetMapping
    List<MatchDayDTO> findAllMatchDays(@RequestParam(value = "dateFrom", required = false) Long dateFrom,
                                       @RequestParam(value = "dateTo", required = false) Long dateTo,
                                       @RequestParam(value = "synced", required = false) Boolean synced);

    @PutMapping
    void updateMatchDay(@RequestBody MatchDayDTO matchDayDTO);
}

package com.pmolinav.prediction.clients;

import com.pmolinav.predictionslib.dto.MatchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "MatchClient", url = "http://predictionsservice:8007", path = "/matches")
public interface MatchClient {

    @GetMapping("/{id}")
    MatchDTO findById(@PathVariable("id") Long id);

    @GetMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    List<MatchDTO> findByCategoryIdSeasonAndMatchDayNumber(
            @PathVariable String categoryId,
            @PathVariable Integer season,
            @PathVariable Integer number);

}
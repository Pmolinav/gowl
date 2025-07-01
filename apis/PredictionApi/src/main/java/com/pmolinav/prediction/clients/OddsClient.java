package com.pmolinav.prediction.clients;

import com.pmolinav.predictionslib.dto.OddsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "OddsClient", url = "http://predictionsservice:8007", path = "/odds")
public interface OddsClient {

    @GetMapping("/{id}")
    OddsDTO findById(@PathVariable("id") Long id);

    @GetMapping("/events/{type}")
    List<OddsDTO> findByEventType(@PathVariable("type") String type);

    @GetMapping("/match/{matchId}")
    List<OddsDTO> findByMatchId(@PathVariable("matchId") Long matchId);

}
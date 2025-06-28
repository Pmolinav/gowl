package com.pmolinav.predictions.clients;

import com.pmolinav.predictionslib.dto.OddsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "OddsClient", url = "http://predictionsservice:8007", path = "/odds")
public interface OddsClient {

    @GetMapping
    List<OddsDTO> findAll();

    @GetMapping("/{id}")
    OddsDTO findById(@PathVariable("id") Long id);

    @GetMapping("/events/{type}")
    List<OddsDTO> findByEventType(@PathVariable("type") String type);

    @PostMapping
    Long create(@RequestBody OddsDTO oddsDTO);

    @PutMapping("/{id}")
    void update(@PathVariable("id") Long id, @RequestBody OddsDTO oddsDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);
}
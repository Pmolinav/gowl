package com.pmolinav.prediction.clients;

import com.pmolinav.predictionslib.dto.EventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "EventClient", url = "http://predictionsservice:8007", path = "/events")
public interface EventClient {

    @GetMapping
    List<EventDTO> findAll();

    @GetMapping("/{type}")
    EventDTO findByEventType(@PathVariable("type") String type);

    @GetMapping("/match/{matchId}")
    List<EventDTO> findEventsByMatchId(@PathVariable("matchId") Long matchId);

    @PostMapping
    Long create(@RequestBody EventDTO eventDTO);

    @PutMapping("/{id}")
    void update(@PathVariable("id") Long id, @RequestBody EventDTO eventDTO);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);
}
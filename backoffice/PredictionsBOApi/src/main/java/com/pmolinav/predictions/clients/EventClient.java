package com.pmolinav.predictions.clients;

import com.pmolinav.predictionslib.dto.EventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "EventClient", url = "http://predictionsservice:8007", path = "/events")
public interface EventClient {

    @GetMapping
    List<EventDTO> findAll();

    @GetMapping("/{type}")
    EventDTO findByType(@PathVariable("type") String type);

    @PostMapping
    Long create(@RequestBody EventDTO eventDTO);

    @PutMapping("/{type}")
    void update(@PathVariable("type") String type, @RequestBody EventDTO eventDTO);

    @DeleteMapping("/{type}")
    void delete(@PathVariable("type") String type);
}
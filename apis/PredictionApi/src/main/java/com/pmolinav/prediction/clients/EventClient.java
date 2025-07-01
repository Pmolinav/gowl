package com.pmolinav.prediction.clients;

import com.pmolinav.predictionslib.dto.EventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "EventClient", url = "http://predictionsservice:8007", path = "/events")
public interface EventClient {

    @GetMapping("/{type}")
    EventDTO findByEventType(@PathVariable("type") String type);
}
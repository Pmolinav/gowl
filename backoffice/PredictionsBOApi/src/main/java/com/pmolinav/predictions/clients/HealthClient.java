package com.pmolinav.predictions.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "HealthClient", url = "http://predictionsservice:8007", path = "/actuator")
public interface HealthClient {

    @GetMapping("/health")
    void health();

}
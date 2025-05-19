package com.pmolinav.leagues.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "HealthClient", url = "leaguesservice:8004/actuator")
public interface HealthClient {

    @GetMapping("/health")
    void health();
}

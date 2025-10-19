package com.pmolinav.auth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "HealthClient", url = "usersservice:8001/actuator")
public interface HealthClient {

    @GetMapping("/health")
    void health();
}

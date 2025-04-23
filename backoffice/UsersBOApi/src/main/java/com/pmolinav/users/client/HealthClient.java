package com.pmolinav.users.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "HealthService", url = "bookingsservice:8001/actuator")
public interface HealthClient {

    @GetMapping("/health")
    void health();
}

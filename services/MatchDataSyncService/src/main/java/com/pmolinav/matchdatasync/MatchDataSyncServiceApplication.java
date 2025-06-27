package com.pmolinav.matchdatasync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories("com.pmolinav.matchdatasync.repositories")
@EnableFeignClients(basePackages = "com.pmolinav.matchdatasync.clients")
@EntityScan("com.pmolinav.predictionslib.*")
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.pmolinav.matchdatasync", "com.pmolinav.predictionslib"})
@EnableAsync
public class MatchDataSyncServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchDataSyncServiceApplication.class, args);
    }

}

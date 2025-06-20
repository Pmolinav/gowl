package com.pmolinav.matchdatasync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories("com.pmolinav.predictions.repositories")
@EntityScan("com.pmolinav.predictionslib.*")
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.pmolinav.predictions", "com.pmolinav.predictionslib"})
public class MatchDataSyncServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchDataSyncServiceApplication.class, args);
    }

}

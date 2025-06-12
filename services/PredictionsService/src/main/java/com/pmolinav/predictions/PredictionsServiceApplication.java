package com.pmolinav.predictions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.pmolinav.predictions.repositories")
@EntityScan("com.pmolinav.predictionslib.*")
@SpringBootApplication(scanBasePackages = {"com.pmolinav.predictions", "com.pmolinav.predictionslib"})
public class PredictionsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PredictionsServiceApplication.class, args);
    }

}

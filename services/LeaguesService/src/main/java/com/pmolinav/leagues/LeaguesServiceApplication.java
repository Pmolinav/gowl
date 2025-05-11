package com.pmolinav.leagues;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.pmolinav.leagues.repositories")
@EntityScan("com.pmolinav.leagueslib.*")
@SpringBootApplication(scanBasePackages = {"com.pmolinav.leagues", "com.pmolinav.leagueslib"})
public class LeaguesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaguesServiceApplication.class, args);
    }

}

package com.pmolinav.bookings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.pmolinav.bookings.*")
@EntityScan("com.pmolinav.userslib.*")
@SpringBootApplication(scanBasePackages = {"com.pmolinav.bookings", "com.pmolinav.userslib"})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}

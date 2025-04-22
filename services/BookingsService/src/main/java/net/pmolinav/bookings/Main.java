package net.pmolinav.bookings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("net.pmolinav.bookings.*")
@EntityScan("net.pmolinav.bookingslib.*")
@SpringBootApplication(scanBasePackages = {"net.pmolinav.bookings", "net.pmolinav.bookingslib"})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}

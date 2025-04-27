package com.pmolinav.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableJpaRepositories("com.pmolinav.users.repositories")
//@EntityScan("com.pmolinav.userslib.*")
@SpringBootApplication(scanBasePackages = {"com.pmolinav.users", "com.pmolinav.userslib"})
public class UsersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersServiceApplication.class, args);
    }

}

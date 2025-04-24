package com.pmolinav.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.pmolinav.users.clients")
public class UsersBOApiMain {

    public static void main(String[] args) {
        SpringApplication.run(UsersBOApiMain.class, args);
    }

}

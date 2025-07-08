package com.pmolinav.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.pmolinav.users.clients")
@EnableDiscoveryClient
@EnableMethodSecurity
public class UsersBOApiMain {

    public static void main(String[] args) {
        SpringApplication.run(UsersBOApiMain.class, args);
    }

}

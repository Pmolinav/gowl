package com.pmolinav.auth;

import com.pmolinav.auth.auth.TokenConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.pmolinav.auth.clients")
@EnableDiscoveryClient
@EnableConfigurationProperties(TokenConfig.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.pmolinav.auth", "com.pmolinav.userslib"})
public class AuthApiMain {

    public static void main(String[] args) {
        SpringApplication.run(AuthApiMain.class, args);
    }

}

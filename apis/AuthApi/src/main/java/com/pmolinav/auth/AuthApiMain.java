package com.pmolinav.auth;

import com.pmolinav.auth.auth.TokenConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.pmolinav.auth.clients")
@EnableConfigurationProperties(TokenConfig.class)
public class AuthApiMain {

    public static void main(String[] args) {
        SpringApplication.run(AuthApiMain.class, args);
    }

}

package com.pmolinav.prediction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.pmolinav.prediction.clients")
@EnableMethodSecurity
public class PredictionApiMain {

    public static void main(String[] args) {
        SpringApplication.run(PredictionApiMain.class, args);
    }

}

package com.pmolinav.predictions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.pmolinav.predictions.clients")
@EnableMethodSecurity
public class PredictionsBOApiMain {

    public static void main(String[] args) {
        SpringApplication.run(PredictionsBOApiMain.class, args);
    }

}

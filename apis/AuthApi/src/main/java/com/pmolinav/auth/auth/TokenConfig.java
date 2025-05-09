package com.pmolinav.auth.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "token")
public class TokenConfig {

    // Getters y Setters
    private String secret;
    private Long validitySeconds;

}

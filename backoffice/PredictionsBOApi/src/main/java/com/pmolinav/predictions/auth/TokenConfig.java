package com.pmolinav.predictions.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "token")
public class TokenConfig {

    private String secret;
    private Long validitySeconds;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getValiditySeconds() {
        return validitySeconds;
    }

    public void setValiditySeconds(Long validitySeconds) {
        this.validitySeconds = validitySeconds;
    }
}


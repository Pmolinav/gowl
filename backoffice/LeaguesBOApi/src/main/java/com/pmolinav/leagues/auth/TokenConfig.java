package com.pmolinav.leagues.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "token")
public class TokenConfig {

    private String secret;
    private Long validitySeconds;
    private Long refreshValiditySeconds;

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

    public Long getRefreshValiditySeconds() {
        return refreshValiditySeconds;
    }

    public void setRefreshValiditySeconds(Long refreshValiditySeconds) {
        this.refreshValiditySeconds = refreshValiditySeconds;
    }
}


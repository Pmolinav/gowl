package com.pmolinav.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.token")
public class AuthProperties {

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

package com.pmolinav.auth.dto;

public enum MDCCommonKeys {
    CORRELATION_UID("Correlation-Uid"),
    REQUEST_UID("Request-Uid"),
    ELAPSED_TIME("Elapsed-time"),
    USERNAME("User"),
    USER_IP("User-IP"),
    USER_AGENT("User-Agent"),
    LEAGUE_ID("League-Id");

    private final String key;

    MDCCommonKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}


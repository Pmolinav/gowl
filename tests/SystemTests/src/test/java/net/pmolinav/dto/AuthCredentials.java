package com.pmolinav.dto;

import javax.validation.constraints.NotNull;

public class AuthCredentials {
    @NotNull
    private String username;
    @NotNull
    private String password;

    public AuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public @NotNull String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }
}

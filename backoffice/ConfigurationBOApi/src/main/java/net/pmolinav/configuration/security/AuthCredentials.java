package net.pmolinav.configuration.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthCredentials {
    @NotNull
    private String username;
    @NotNull
    private String password;

}

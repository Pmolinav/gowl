package com.pmolinav.userslib.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LogoutDTO {

    @NotBlank(message = "Username is mandatory.")
    private String username;

    @NotBlank(message = "refreshToken is mandatory.")
    private String refreshToken;

}

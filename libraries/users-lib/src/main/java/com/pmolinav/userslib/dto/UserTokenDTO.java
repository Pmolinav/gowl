package com.pmolinav.userslib.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserTokenDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String refreshToken;

    private String deviceInfo;

    private String ipAddress;

    @Future
    private LocalDateTime expiresAt;

}

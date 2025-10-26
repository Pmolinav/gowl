package com.pmolinav.userslib.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserPublicDTO {

    @NotBlank(message = "Username is mandatory.")
    private String username;

    @ToString.Exclude
    @NotBlank(message = "Password is mandatory.")
    private String password;

    @NotBlank(message = "Name is mandatory.")
    private String name;

    @NotBlank(message = "Email is mandatory.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

}

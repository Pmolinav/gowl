package com.pmolinav.userslib.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UpdateUserDTO {

    private String name;

    @Email(message = "Invalid email format.")
    private String email;

    private LocalDate birthDate;

}

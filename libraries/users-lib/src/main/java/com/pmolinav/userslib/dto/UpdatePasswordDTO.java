package com.pmolinav.userslib.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UpdatePasswordDTO {

    @NotBlank(message = "oldPassword is mandatory.")
    private String oldPassword;

    @NotBlank(message = "newPassword is mandatory.")
    private String newPassword;


}

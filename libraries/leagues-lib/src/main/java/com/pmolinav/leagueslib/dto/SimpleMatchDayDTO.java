package com.pmolinav.leagueslib.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SimpleMatchDayDTO {

    @NotBlank(message = "Label cannot be blank")
    @Size(max = 10, message = "Label must be at most 10 characters long")
    private String label;

    @NotBlank(message = "Value cannot be blank")
    private String value;

}

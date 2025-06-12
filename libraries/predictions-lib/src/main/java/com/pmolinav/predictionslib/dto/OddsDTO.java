package com.pmolinav.predictionslib.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class OddsDTO {

    private Long oddsId;

    @NotNull(message = "Event ID must not be null.")
    private Long eventId;

    @NotBlank(message = "Label is mandatory.")
    @Size(max = 50, message = "Label must be at most 50 characters.")
    private String label;

    @NotNull(message = "Odds value must not be null.")
    @DecimalMin(value = "1.00", message = "Odds must be at least 1.00.")
    private BigDecimal value;

    private Boolean active = true;

}

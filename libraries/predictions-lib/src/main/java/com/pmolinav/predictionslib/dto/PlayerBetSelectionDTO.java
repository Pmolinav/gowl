package com.pmolinav.predictionslib.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PlayerBetSelectionDTO {

    @NotNull(message = "Event type must not be null.")
    private String eventType;

    @NotNull(message = "Odds ID must not be null.")
    private Long oddsId;

    @NotNull(message = "Stake must not be null.")
    @DecimalMin(value = "0.01", message = "Stake must be at least 0.01.")
    private BigDecimal stake;

}

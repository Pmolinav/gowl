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

    @NotNull(message = "Event ID must not be null.")
    private Long eventId;

    @NotBlank(message = "Label is mandatory.")
    @Size(max = 50, message = "Label must be at most 50 characters.")
    private String label;

    @NotNull(message = "Odds value must not be null.")
    @DecimalMin(value = "1.00", message = "Odds must be at least 1.00.")
    private BigDecimal value;

    private BigDecimal point;

    private Boolean active = true;

    public OddsDTO(Long eventId, String label, BigDecimal value, Boolean active) {
        this.eventId = eventId;
        this.label = label;
        this.value = value;
        this.active = active;
    }
}

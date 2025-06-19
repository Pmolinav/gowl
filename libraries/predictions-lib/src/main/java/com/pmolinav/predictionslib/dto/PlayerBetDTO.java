package com.pmolinav.predictionslib.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PlayerBetDTO {

    @NotBlank(message = "Username is mandatory.")
    @Size(max = 100, message = "Username must be at most 100 characters.")
    private String username;

    @NotNull(message = "Match ID must not be null.")
    private Long matchId;

    private BigDecimal totalStake;

    @NotNull(message = "Selections must not be null.")
    private List<PlayerBetSelectionDTO> selections;

}

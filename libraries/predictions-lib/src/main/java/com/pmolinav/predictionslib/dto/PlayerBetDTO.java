package com.pmolinav.predictionslib.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private PlayerBetStatus status = PlayerBetStatus.PENDING;

    @NotNull(message = "Selections must not be null.")
    private List<PlayerBetSelectionDTO> selections;

    public PlayerBetDTO(String username, Long matchId, BigDecimal totalStake, List<PlayerBetSelectionDTO> selections) {
        this.username = username;
        this.matchId = matchId;
        this.totalStake = totalStake;
        this.selections = selections;
    }

    @JsonIgnore
    public BigDecimal getCalculatedStake() {
        return totalStake != null ? totalStake :
                selections.stream()
                        .map(PlayerBetSelectionDTO::getStake)
                        .reduce(BigDecimal.ONE, BigDecimal::multiply);
    }

    @JsonIgnore
    public void setCalculatedStake() {
        this.totalStake = selections.stream()
                .map(PlayerBetSelectionDTO::getStake)
                .reduce(BigDecimal.ONE, BigDecimal::multiply);
    }
}

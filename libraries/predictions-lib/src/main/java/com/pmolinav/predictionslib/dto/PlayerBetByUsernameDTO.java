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
public class PlayerBetByUsernameDTO {

    @NotBlank(message = "Username is mandatory.")
    @Size(max = 100, message = "Username must be at most 100 characters.")
    private String username;

    @NotNull(message = "Match must not be null.")
    private SimpleMatchDTO match;

    @NotNull(message = "League ID must not be null.")
    private Long leagueId;

    private BigDecimal totalStake;

    private PlayerBetStatus status = PlayerBetStatus.PENDING;

    private Long creationDate;

    @NotNull(message = "Selections must not be null.")
    private List<PlayerBetSelectionByUsernameDTO> selections;

    public PlayerBetByUsernameDTO(String username, SimpleMatchDTO match, Long leagueId, BigDecimal totalStake, List<PlayerBetSelectionByUsernameDTO> selections) {
        this.username = username;
        this.match = match;
        this.leagueId = leagueId;
        this.totalStake = totalStake;
        this.selections = selections;
    }

    public PlayerBetByUsernameDTO(String username, SimpleMatchDTO match, Long leagueId, BigDecimal totalStake, Long creationDate, List<PlayerBetSelectionByUsernameDTO> selections) {
        this.username = username;
        this.match = match;
        this.leagueId = leagueId;
        this.totalStake = totalStake;
        this.creationDate = creationDate;
        this.selections = selections;
    }

    @JsonIgnore
    public BigDecimal getCalculatedStake() {
        return totalStake != null ? totalStake :
                selections.stream()
                        .map(PlayerBetSelectionByUsernameDTO::getStake)
                        .reduce(BigDecimal.ONE, BigDecimal::multiply);
    }

    @JsonIgnore
    public void setCalculatedStake() {
        this.totalStake = selections.stream()
                .map(PlayerBetSelectionByUsernameDTO::getStake)
                .reduce(BigDecimal.ONE, BigDecimal::multiply);
    }
}

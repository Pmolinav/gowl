package com.pmolinav.leagueslib.dto;

import com.pmolinav.leagueslib.model.PlayerStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LeaguePlayerDTO {

    private Long leagueId;

    @NotBlank(message = "Username is required.")
    @Size(max = 100, message = "Username must not exceed 100 characters.")
    private String username;

    @NotNull(message = "Total points are required.")
    @Min(value = 0, message = "Total points must be greater than or equal to 0.")
    private Integer totalPoints;

    private PlayerStatus playerStatus = PlayerStatus.ACTIVE;

    public LeaguePlayerDTO(String username, Integer totalPoints, PlayerStatus playerStatus) {
        this.username = username;
        this.totalPoints = totalPoints;
        this.playerStatus = playerStatus;
    }
}

package com.pmolinav.leagueslib.dto;

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
public class LeaguePlayerPointsDTO {

    @NotBlank(message = "Category ID must not be blank")
    @Size(max = 50, message = "Category ID must be at most 50 characters long")
    private String categoryId;

    @NotNull(message = "Season must not be null")
    @Min(value = 0, message = "Season must be a non-negative number")
    private Integer season;

    @NotNull(message = "Match day number must not be null")
    @Min(value = 1, message = "Match day number must be at least 1")
    private Integer matchDayNumber;

    @NotNull(message = "League ID must not be null")
    private Long leagueId;

    @NotBlank(message = "Username must not be blank")
    @Size(max = 100, message = "Username must be at most 100 characters long")
    private String username;

    @NotNull(message = "Points must not be null")
    private Integer points;

}

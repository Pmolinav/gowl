package com.pmolinav.predictionslib.dto;

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
public class MatchDTO {

    private Long matchId;

    @NotNull(message = "Category ID must not be null.")
    private String categoryId;

    @NotNull(message = "Season must not be null.")
    private Integer season;

    @NotNull(message = "MatchDay number must not be null.")
    private Integer matchDayNumber;

    @NotBlank(message = "Home team name is mandatory.")
    @Size(max = 100, message = "Home team name must be at most 100 characters.")
    private String homeTeam;

    @NotBlank(message = "Away team name is mandatory.")
    @Size(max = 100, message = "Away team name must be at most 100 characters.")
    private String awayTeam;

    @NotNull(message = "Start time must not be null.")
    private Long startTime;

    @Size(max = 20, message = "Status must be at most 20 characters.")
    private String status = "ACTIVE";

}

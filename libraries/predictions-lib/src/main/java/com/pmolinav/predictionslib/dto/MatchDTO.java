package com.pmolinav.predictionslib.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MatchDTO {

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

    private MatchStatus status = MatchStatus.SCHEDULED;

    @Size(max = 100, message = "External ID must be at most 100 characters.")
    private String externalId;

    private List<OddsDTO> odds;

    public MatchDTO(String categoryId, Integer season, Integer matchDayNumber, String homeTeam, String awayTeam, Long startTime, MatchStatus status) {
        this.categoryId = categoryId;
        this.season = season;
        this.matchDayNumber = matchDayNumber;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = startTime;
        this.status = status;
    }

    public MatchDTO(String categoryId, Integer season, Integer matchDayNumber, String homeTeam, String awayTeam, Long startTime, MatchStatus status, List<OddsDTO> odds) {
        this.categoryId = categoryId;
        this.season = season;
        this.matchDayNumber = matchDayNumber;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = startTime;
        this.status = status;
        this.odds = odds;
    }
}

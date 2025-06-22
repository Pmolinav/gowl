package com.pmolinav.predictionslib.model;

import com.pmolinav.predictionslib.dto.MatchStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "match", indexes = {
        @Index(name = "idx_category_season_day", columnList = "category_id, season, match_day_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "category_id", length = 50, nullable = false)
    private String categoryId;

    @Column(name = "season", nullable = false)
    private Integer season;

    @Column(name = "match_day_number", nullable = false)
    private Integer matchDayNumber;

    @Column(name = "home_team", nullable = false, length = 100)
    private String homeTeam;

    @Column(name = "away_team", nullable = false, length = 100)
    private String awayTeam;

    @Column(name = "start_time", nullable = false)
    private Long startTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "creation_date")
    private Long creationDate;

    @Column(name = "modification_date")
    private Long modificationDate;

    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY)
    private List<Event> events;

    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY)
    private List<PlayerBet> playerBets;

    public Match(Long matchId, String categoryId, Integer season, Integer matchDayNumber, String homeTeam, String awayTeam, Long startTime, MatchStatus status, Long creationDate, Long modificationDate) {
        this.matchId = matchId;
        this.categoryId = categoryId;
        this.season = season;
        this.matchDayNumber = matchDayNumber;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = startTime;
        this.status = status;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public Match(Long matchId, String categoryId, Integer season, Integer matchDayNumber, String homeTeam, String awayTeam, Long startTime, MatchStatus status, String externalId, Long creationDate, Long modificationDate) {
        this.matchId = matchId;
        this.categoryId = categoryId;
        this.season = season;
        this.matchDayNumber = matchDayNumber;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = startTime;
        this.status = status;
        this.externalId = externalId;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(matchId, match.matchId)
                && Objects.equals(categoryId, match.categoryId)
                && Objects.equals(season, match.season)
                && Objects.equals(matchDayNumber, match.matchDayNumber)
                && Objects.equals(homeTeam, match.homeTeam)
                && Objects.equals(awayTeam, match.awayTeam)
                && Objects.equals(startTime, match.startTime)
                && Objects.equals(status, match.status)
                && Objects.equals(externalId, match.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId, categoryId, season, matchDayNumber, homeTeam, awayTeam, startTime, status, externalId);
    }
}

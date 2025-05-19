package com.pmolinav.leagueslib.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(
        name = "league_player_points",
        indexes = {
                @Index(name = "idx_lpp_league_user", columnList = "league_id, username"),
                @Index(name = "idx_lpp_matchday", columnList = "category_id, season, match_day_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(LeaguePlayerPointsId.class)
public class LeaguePlayerPoints {

    @Id
    @Column(name = "category_id", length = 50, nullable = false)
    private String categoryId;

    @Id
    @Column(name = "season", nullable = false)
    private Integer season;

    @Id
    @Column(name = "match_day_number", nullable = false)
    private Integer matchDayNumber;

    @Id
    @Column(name = "league_id", nullable = false)
    private Long leagueId;

    @Id
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "points", nullable = false)
    private Integer points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "category_id", referencedColumnName = "category_id", insertable = false, updatable = false),
            @JoinColumn(name = "season", referencedColumnName = "season", insertable = false, updatable = false),
            @JoinColumn(name = "match_day_number", referencedColumnName = "match_day_number", insertable = false, updatable = false)
    })
    private MatchDay matchDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "league_id", referencedColumnName = "league_id", insertable = false, updatable = false),
            @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    })
    private LeaguePlayer leaguePlayer;

    public LeaguePlayerPoints(String categoryId, Integer season, Integer matchDayNumber, Long leagueId, String username, Integer points) {
        this.categoryId = categoryId;
        this.season = season;
        this.matchDayNumber = matchDayNumber;
        this.leagueId = leagueId;
        this.username = username;
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaguePlayerPoints that = (LeaguePlayerPoints) o;
        return Objects.equals(categoryId, that.categoryId) && Objects.equals(season, that.season) && Objects.equals(matchDayNumber, that.matchDayNumber) && Objects.equals(leagueId, that.leagueId) && Objects.equals(username, that.username) && Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, season, matchDayNumber, leagueId, username, points);
    }
}


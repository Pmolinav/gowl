package com.pmolinav.leagueslib.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(
        name = "league_player",
        indexes = {
                @Index(name = "idx_leagueplayer_league", columnList = "league_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(LeaguePlayerId.class)
public class LeaguePlayer {

    @Id
    @Column(name = "league_id", nullable = false)
    private Long leagueId;

    @Id
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @Enumerated(EnumType.STRING)
    @Column(name = "player_status", nullable = false)
    private PlayerStatus playerStatus;

    @Column(name = "join_date", nullable = false)
    private Long joinDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", insertable = false, updatable = false)
    private League league;

    public LeaguePlayer(Long leagueId, String username, Integer totalPoints, PlayerStatus playerStatus, Long joinDate) {
        this.leagueId = leagueId;
        this.username = username;
        this.totalPoints = totalPoints;
        this.playerStatus = playerStatus;
        this.joinDate = joinDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaguePlayer that = (LeaguePlayer) o;
        return Objects.equals(leagueId, that.leagueId)
                && Objects.equals(username, that.username)
                && Objects.equals(totalPoints, that.totalPoints)
                && playerStatus == that.playerStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leagueId, username, totalPoints, playerStatus);
    }
}

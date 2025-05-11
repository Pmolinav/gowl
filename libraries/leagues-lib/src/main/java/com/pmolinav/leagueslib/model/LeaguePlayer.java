package com.pmolinav.leagueslib.model;

import jakarta.persistence.*;
import lombok.*;

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
@EqualsAndHashCode
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
}

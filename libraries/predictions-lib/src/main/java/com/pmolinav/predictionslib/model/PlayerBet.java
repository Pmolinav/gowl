package com.pmolinav.predictionslib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pmolinav.predictionslib.dto.PlayerBetStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "player_bet", indexes = {
        @Index(name = "idx_player_bet_username", columnList = "username"),
        @Index(name = "idx_player_bet_match_id", columnList = "match_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlayerBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bet_id")
    private Long betId;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "league_id", nullable = false)
    private Long leagueId;

    @Column(name = "total_stake", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalStake;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PlayerBetStatus status = PlayerBetStatus.PENDING;

    @Column(name = "creation_date")
    private Long creationDate;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false, insertable = false, updatable = false)
    private Match match;

    @OneToMany(mappedBy = "playerBet", fetch = FetchType.LAZY)
    private List<PlayerBetSelection> selections;

    public PlayerBet(Long betId, String username, Long matchId, Long leagueId, BigDecimal totalStake, PlayerBetStatus status, Long creationDate) {
        this.betId = betId;
        this.username = username;
        this.matchId = matchId;
        this.leagueId = leagueId;
        this.totalStake = totalStake;
        this.status = status;
        this.creationDate = creationDate;
    }

    public PlayerBet(Long betId, String username, Long matchId, Long leagueId, BigDecimal totalStake, Long creationDate) {
        this.betId = betId;
        this.username = username;
        this.matchId = matchId;
        this.leagueId = leagueId;
        this.totalStake = totalStake;
        this.creationDate = creationDate;
    }

    public BigDecimal getCalculatedStake() {
        return totalStake != null ? totalStake :
                selections.stream()
                        .map(PlayerBetSelection::getStake)
                        .reduce(BigDecimal.ONE, BigDecimal::multiply);
    }

    @JsonIgnore
    public void setCalculatedStake() {
        this.totalStake = selections.stream()
                .map(PlayerBetSelection::getStake)
                .reduce(BigDecimal.ONE, BigDecimal::multiply);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerBet playerBet = (PlayerBet) o;
        return Objects.equals(betId, playerBet.betId)
                && Objects.equals(username, playerBet.username)
                && Objects.equals(matchId, playerBet.matchId)
                && Objects.equals(leagueId, playerBet.leagueId)
                && Objects.equals(totalStake, playerBet.totalStake);
    }

    @Override
    public int hashCode() {
        return Objects.hash(betId, username, matchId, leagueId, totalStake);
    }
}
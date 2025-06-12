package com.pmolinav.predictionslib.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "player_bet_selection", indexes = {
        @Index(name = "idx_player_bet_selection_bet_id", columnList = "bet_id"),
        @Index(name = "idx_player_bet_selection_odds_id", columnList = "odds_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlayerBetSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selection_id")
    private Long selectionId;

    @Column(name = "bet_id", nullable = false)
    private Long betId;

    @Column(name = "odds_id", nullable = false)
    private Long oddsId;

    @Column(name = "stake", nullable = false, precision = 10, scale = 2)
    private BigDecimal stake;

    @Column(name = "creation_date")
    private Long creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bet_id", nullable = false, insertable = false, updatable = false)
    private PlayerBet playerBet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odds_id", nullable = false, insertable = false, updatable = false)
    private Odds odds;

    public PlayerBetSelection(Long selectionId, Long betId, Long oddsId, BigDecimal stake, Long creationDate) {
        this.selectionId = selectionId;
        this.betId = betId;
        this.oddsId = oddsId;
        this.stake = stake;
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerBetSelection that = (PlayerBetSelection) o;
        return Objects.equals(selectionId, that.selectionId)
                && Objects.equals(betId, that.betId)
                && Objects.equals(oddsId, that.oddsId)
                && Objects.equals(stake, that.stake);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selectionId, betId, oddsId, stake);
    }
}
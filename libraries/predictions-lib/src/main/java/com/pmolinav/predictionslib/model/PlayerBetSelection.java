package com.pmolinav.predictionslib.model;

import com.pmolinav.predictionslib.dto.PlayerBetStatus;
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

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "odds_id", nullable = false)
    private Long oddsId;

    @Column(name = "stake", nullable = false, precision = 10, scale = 2)
    private BigDecimal stake;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PlayerBetStatus status = PlayerBetStatus.PENDING;

    @Column(name = "creation_date")
    private Long creationDate;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bet_id", nullable = false, insertable = false, updatable = false)
    private PlayerBet playerBet;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "odds_id", nullable = false, insertable = false, updatable = false)
    private Odds odds;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type", nullable = false, insertable = false, updatable = false)
    private Event event;

    public PlayerBetSelection(Long selectionId, Long betId, String eventType, Long oddsId, BigDecimal stake, PlayerBetStatus status, Long creationDate) {
        this.selectionId = selectionId;
        this.betId = betId;
        this.eventType = eventType;
        this.oddsId = oddsId;
        this.stake = stake;
        this.status = status;
        this.creationDate = creationDate;
    }

    public PlayerBetSelection(Long selectionId, Long betId, String eventType, Long oddsId, BigDecimal stake, Long creationDate) {
        this.selectionId = selectionId;
        this.betId = betId;
        this.eventType = eventType;
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
                && Objects.equals(eventType, that.eventType)
                && Objects.equals(oddsId, that.oddsId)
                && Objects.equals(stake, that.stake)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selectionId, betId, eventType, oddsId, stake, status);
    }
}
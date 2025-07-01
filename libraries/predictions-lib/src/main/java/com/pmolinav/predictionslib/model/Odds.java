package com.pmolinav.predictionslib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "odds", indexes = {
        @Index(name = "idx_odds_event_type", columnList = "event_type"),
        @Index(name = "idx_odds_match_id", columnList = "match_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Odds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "odds_id")
    private Long oddsId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "label", nullable = false, length = 50)
    private String label;

    @Column(name = "value", nullable = false, precision = 6, scale = 2)
    private BigDecimal value;

    @Column(name = "point", precision = 6, scale = 2)
    private BigDecimal point;

    @Column(name = "provider", length = 50)
    private String provider;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "creation_date")
    private Long creationDate;

    @Column(name = "modification_date")
    private Long modificationDate;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type", nullable = false, insertable = false, updatable = false)
    private Event event;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", insertable = false, updatable = false)
    private Match match;

    public Odds(Long oddsId, String eventType, Long matchId, String label, BigDecimal value, String provider, Boolean active, Long creationDate, Long modificationDate) {
        this.oddsId = oddsId;
        this.eventType = eventType;
        this.matchId = matchId;
        this.label = label;
        this.value = value;
        this.provider = provider;
        this.active = active;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public Odds(Long oddsId, String eventType, Long matchId, String label, BigDecimal value, BigDecimal point, String provider, Boolean active, Long creationDate, Long modificationDate) {
        this.oddsId = oddsId;
        this.eventType = eventType;
        this.matchId = matchId;
        this.label = label;
        this.value = value;
        this.point = point;
        this.provider = provider;
        this.active = active;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Odds odds = (Odds) o;
        return Objects.equals(oddsId, odds.oddsId)
                && Objects.equals(eventType, odds.eventType)
                && Objects.equals(matchId, odds.matchId)
                && Objects.equals(label, odds.label)
                && Objects.equals(value, odds.value)
                && Objects.equals(point, odds.point)
                && Objects.equals(provider, odds.provider)
                && Objects.equals(active, odds.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oddsId, eventType, matchId, label, value, point, provider, active);
    }
}


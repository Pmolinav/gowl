package com.pmolinav.predictionslib.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "odds", indexes = {
        @Index(name = "idx_odds_event_id", columnList = "event_id")
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

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "label", nullable = false, length = 50)
    private String label;

    @Column(name = "value", nullable = false, precision = 6, scale = 2)
    private BigDecimal value;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "creation_date")
    private Long creationDate;

    @Column(name = "modification_date")
    private Long modificationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false, insertable = false, updatable = false)
    private Event event;

    public Odds(Long oddsId, Long eventId, String label, BigDecimal value, Boolean active, Long creationDate, Long modificationDate) {
        this.oddsId = oddsId;
        this.eventId = eventId;
        this.label = label;
        this.value = value;
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
                && Objects.equals(eventId, odds.eventId)
                && Objects.equals(label, odds.label)
                && Objects.equals(value, odds.value)
                && Objects.equals(active, odds.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oddsId, eventId, label, value, active);
    }
}


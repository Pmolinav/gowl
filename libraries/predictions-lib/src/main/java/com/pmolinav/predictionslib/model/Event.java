package com.pmolinav.predictionslib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "event", indexes = {
        @Index(name = "idx_event_match_id", columnList = "match_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event {

    @Id
    @Column(name = "event_type", length = 100)
    private String eventType;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private Long creationDate;

    @Column(name = "modification_date")
    private Long modificationDate;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", insertable = false, updatable = false)
    private Match match;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Odds> oddsList;

    public Event(String eventType, Long matchId, String description, Long creationDate, Long modificationDate) {
        this.eventType = eventType;
        this.matchId = matchId;
        this.description = description;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventType, event.eventType)
                && Objects.equals(matchId, event.matchId)
                && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, matchId, description);
    }
}


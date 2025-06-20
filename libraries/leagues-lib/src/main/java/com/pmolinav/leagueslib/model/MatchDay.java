package com.pmolinav.leagueslib.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(
        name = "match_day",
        indexes = {
                @Index(name = "idx_matchday_category", columnList = "category_id"),
                @Index(name = "idx_matchday_startdate_synced", columnList = "start_date, synced")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@IdClass(MatchDayId.class)
public class MatchDay {

    @Id
    @Column(name = "category_id", length = 50, nullable = false)
    private String categoryId;

    @Id
    @Column(name = "season", nullable = false)
    private Integer season;

    @Id
    @Column(name = "match_day_number", nullable = false)
    private Integer matchDayNumber;

    @Column(name = "start_date", nullable = false)
    private Long startDate;

    @Column(name = "end_date", nullable = false)
    private Long endDate;

    @Column(name = "synced", nullable = false)
    private boolean synced = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private LeagueCategory category;

    public MatchDay(String categoryId, Integer season, Integer matchDayNumber, Long startDate, Long endDate, boolean synced) {
        this.categoryId = categoryId;
        this.season = season;
        this.matchDayNumber = matchDayNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.synced = synced;
    }

    public MatchDay(String categoryId, Integer season, Integer matchDayNumber, Long startDate, Long endDate) {
        this.categoryId = categoryId;
        this.season = season;
        this.matchDayNumber = matchDayNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchDay matchDay = (MatchDay) o;
        return Objects.equals(categoryId, matchDay.categoryId)
                && Objects.equals(season, matchDay.season)
                && Objects.equals(matchDayNumber, matchDay.matchDayNumber)
                && Objects.equals(startDate, matchDay.startDate)
                && Objects.equals(endDate, matchDay.endDate)
                && Objects.equals(synced, matchDay.synced);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, season, matchDayNumber, startDate, endDate, synced);
    }
}
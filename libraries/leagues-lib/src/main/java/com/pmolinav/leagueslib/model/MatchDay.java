package com.pmolinav.leagueslib.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "match_day",
        indexes = {
                @Index(name = "idx_matchday_category", columnList = "category_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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

    @Column(name = "start_date")
    private Long startDate;

    @Column(name = "end_date")
    private Long endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private LeagueCategory category;
}
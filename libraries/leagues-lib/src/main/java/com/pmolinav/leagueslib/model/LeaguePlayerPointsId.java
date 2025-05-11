package com.pmolinav.leagueslib.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LeaguePlayerPointsId implements Serializable {
    private String categoryId;
    private Integer season;
    private Integer matchDayNumber;
    private Long leagueId;
    private String username;
}


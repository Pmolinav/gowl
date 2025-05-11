package com.pmolinav.leagueslib.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LeaguePlayerId implements Serializable {

    private Long leagueId;
    private String username;
}

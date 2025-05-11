package com.pmolinav.leagueslib.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MatchDayId implements Serializable {

    private String categoryId;
    private Integer season;
    private Integer matchDayNumber;
}


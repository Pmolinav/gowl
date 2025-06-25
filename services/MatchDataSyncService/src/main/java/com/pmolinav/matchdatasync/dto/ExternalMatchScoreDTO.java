package com.pmolinav.matchdatasync.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class ExternalMatchScoreDTO {
    private String id;
    private String sport_key;
    private String sport_title;
    private OffsetDateTime commence_time;
    private boolean completed;
    private String home_team;
    private String away_team;
    private List<ExternalScoreDTO> scores;
    private OffsetDateTime last_update;

}
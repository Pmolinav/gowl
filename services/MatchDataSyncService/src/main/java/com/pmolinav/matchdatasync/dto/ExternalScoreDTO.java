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
public class ExternalScoreDTO {
    private String name;
    private String score;

}
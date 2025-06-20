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
public class ExternalMarketDTO {
    private String key;
    private OffsetDateTime last_update;
    private String link;
    private List<ExternalOutcomeDTO> outcomes;

}
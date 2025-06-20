
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
public class ExternalBookmakerDTO {
    private String key;
    private String title;
    private OffsetDateTime last_update;
    private String link;
    private List<ExternalMarketDTO> markets;

}
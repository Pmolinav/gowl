package com.pmolinav.matchdatasync.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class ExternalOutcomeDTO {
    private String name;
    private BigDecimal price;
    private BigDecimal point; // Can be null for some markets.
    private String link;

}
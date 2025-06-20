package com.pmolinav.predictionslib.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "external_odds_mapping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ExternalOddsMapping {

    @Id
    @Column(name = "odds_label")
    private String label;

    @Column(name = "external_market_key")
    private String externalMarketKey;
}


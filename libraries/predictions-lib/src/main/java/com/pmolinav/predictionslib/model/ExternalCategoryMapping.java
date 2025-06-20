package com.pmolinav.predictionslib.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "external_category_mapping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ExternalCategoryMapping {

    @Id
    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "external_sport_key")
    private String externalSportKey;
}


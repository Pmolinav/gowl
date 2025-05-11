package com.pmolinav.leagueslib.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "league_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LeagueCategory {

    @Id
    @Column(name = "category_id", length = 50)
    private String categoryId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "sport", length = 100)
    private String sport;

    @Column(name = "country", length = 2, nullable = false)
    private String country; // ISO 3166-1 alpha-2

    @Column(name = "icon_url", length = 256)
    private String iconUrl;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "creation_date", nullable = false)
    private Long creationDate;

    @Column(name = "modification_date")
    private Long modificationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeagueCategory that = (LeagueCategory) o;
        return isActive == that.isActive
                && Objects.equals(categoryId, that.categoryId)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(sport, that.sport)
                && Objects.equals(country, that.country)
                && Objects.equals(iconUrl, that.iconUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, name, description, sport, country, iconUrl, isActive);
    }
}

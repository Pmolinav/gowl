package com.pmolinav.leagueslib.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "league",
        indexes = {
                @Index(name = "idx_league_category", columnList = "category_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "league_id")
    private Long leagueId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "category_id", nullable = false, length = 50)
    private String categoryId;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeagueStatus status;

    @Column(name = "max_players")
    private Integer maxPlayers;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "is_premium", nullable = false)
    private boolean isPremium;

    @Column(name = "owner_username", nullable = false)
    private String ownerUsername;

    @Column(name = "creation_date", nullable = false)
    private Long creationDate;

    @Column(name = "modification_date")
    private Long modificationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private LeagueCategory category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "league")
    private List<LeaguePlayer> leaguePlayers;

    public League(Long leagueId, String name, String description, String categoryId, boolean isPublic, String password, LeagueStatus status, Integer maxPlayers, String logoUrl, boolean isPremium, String ownerUsername, Long creationDate, Long modificationDate) {
        this.leagueId = leagueId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.isPublic = isPublic;
        this.password = password;
        this.status = status;
        this.maxPlayers = maxPlayers;
        this.logoUrl = logoUrl;
        this.isPremium = isPremium;
        this.ownerUsername = ownerUsername;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public League(String name, String description, String categoryId, boolean isPublic, String password, LeagueStatus status, Integer maxPlayers, String logoUrl, boolean isPremium, String ownerUsername, Long creationDate, Long modificationDate) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.isPublic = isPublic;
        this.password = password;
        this.status = status;
        this.maxPlayers = maxPlayers;
        this.logoUrl = logoUrl;
        this.isPremium = isPremium;
        this.ownerUsername = ownerUsername;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return isPublic == league.isPublic
                && isPremium == league.isPremium
                && Objects.equals(leagueId, league.leagueId)
                && Objects.equals(name, league.name)
                && Objects.equals(description, league.description)
                && Objects.equals(categoryId, league.categoryId)
                && Objects.equals(password, league.password)
                && status == league.status
                && Objects.equals(maxPlayers, league.maxPlayers)
                && Objects.equals(logoUrl, league.logoUrl)
                && Objects.equals(ownerUsername, league.ownerUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leagueId, name, description, categoryId, isPublic, password, status, maxPlayers, logoUrl, isPremium, ownerUsername);
    }
}

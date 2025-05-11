package com.pmolinav.leagueslib.model;

import jakarta.persistence.*;
import lombok.*;

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
@EqualsAndHashCode
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

    @Column(name = "category_id", nullable = false, length = 50, insertable = false, updatable = false)
    private String categoryId;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "password")
    private String passwordHash;

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
    @JoinColumn(name = "category_id", nullable = false)
    private LeagueCategory category;

}

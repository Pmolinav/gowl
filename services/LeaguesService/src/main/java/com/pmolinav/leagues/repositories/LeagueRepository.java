package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Long> {

    Optional<League> findByName(String name);

    List<League> findByLeagueIdIn(List<Long> ids);

    @Modifying
    @Query("UPDATE League l SET l.status = 'CLOSED' WHERE l.leagueId = :leagueId")
    int closeLeagueById(@Param("leagueId") Long leagueId);

    @Modifying
    @Query("UPDATE League l SET l.status = 'CLOSED' WHERE l.name = :name")
    int closeLeagueByName(@Param("name") String name);

    @Query("""
        SELECT l
        FROM League l
        JOIN l.leaguePlayers lp
        WHERE lp.username = :username
    """)
    List<League> findAllByPlayerUsername(@Param("username") String username);
}



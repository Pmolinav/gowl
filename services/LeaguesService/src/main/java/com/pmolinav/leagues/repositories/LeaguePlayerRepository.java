package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.LeaguePlayer;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaguePlayerRepository extends JpaRepository<LeaguePlayer, LeaguePlayerId> {
    List<LeaguePlayer> findByLeagueId(Long leagueId);
    List<LeaguePlayer> findByUsername(String username);
}



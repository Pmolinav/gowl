package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.LeaguePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaguePlayerRepository extends JpaRepository<LeaguePlayer, Long> {
}



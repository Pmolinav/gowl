package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Long> {
}



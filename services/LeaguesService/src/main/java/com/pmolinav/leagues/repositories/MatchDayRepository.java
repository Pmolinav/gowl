package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.League;
import com.pmolinav.leagueslib.model.MatchDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchDayRepository extends JpaRepository<MatchDay, Long> {
}



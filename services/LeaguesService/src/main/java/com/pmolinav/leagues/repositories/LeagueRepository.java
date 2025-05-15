package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Long> {
    Optional<League> findByName(String name);
    List<League> findByLeagueIdIn(List<Long> ids);
}



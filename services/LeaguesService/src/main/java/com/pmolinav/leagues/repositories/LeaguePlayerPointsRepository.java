package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.LeaguePlayerPoints;
import com.pmolinav.leagueslib.model.LeaguePlayerPointsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaguePlayerPointsRepository extends JpaRepository<LeaguePlayerPoints, LeaguePlayerPointsId> {

    List<LeaguePlayerPoints> findByLeagueId(Long leagueId);

    List<LeaguePlayerPoints> findByLeagueIdAndUsername(Long leagueId, String username);

    List<LeaguePlayerPoints> findByCategoryIdAndSeasonAndMatchDayNumber(String categoryId, Integer season, Integer matchDayNumber);

}



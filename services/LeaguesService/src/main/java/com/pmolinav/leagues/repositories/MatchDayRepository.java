package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.MatchDay;
import com.pmolinav.leagueslib.model.MatchDayId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchDayRepository extends JpaRepository<MatchDay, MatchDayId> {

    List<MatchDay> findByCategoryId(String categoryId);

    List<MatchDay> findByCategoryIdAndSeason(String categoryId, Integer season);
}



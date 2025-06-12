package com.pmolinav.predictions.repositories;


import com.pmolinav.predictionslib.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByCategoryIdAndSeasonAndMatchDayNumber(String categoryId, Integer season, Integer matchDayNumber);
}

package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.MatchDay;
import com.pmolinav.leagueslib.model.MatchDayId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchDayRepository extends JpaRepository<MatchDay, MatchDayId> {

    List<MatchDay> findByCategoryId(String categoryId);

    List<MatchDay> findByCategoryIdAndSeason(String categoryId, Integer season);

    List<MatchDay> findByStartDateBetweenAndSynced(Long dateFrom, Long dateTo, boolean synced);

    List<MatchDay> findByStartDateBetween(Long dateFrom, Long dateTo);

    List<MatchDay> findByStartDateGreaterThanEqualAndSynced(Long dateFrom, boolean synced);

    List<MatchDay> findByStartDateLessThanEqualAndSynced(Long dateTo, boolean synced);

    List<MatchDay> findByStartDateGreaterThanEqual(Long dateFrom);

    List<MatchDay> findByStartDateLessThanEqual(Long dateTo);

    List<MatchDay> findBySynced(boolean synced);

    List<MatchDay> findByEndDateBetweenAndResultsChecked(Long from, Long to, Boolean resultsChecked);

    List<MatchDay> findByEndDateBetween(Long from, Long to);

    List<MatchDay> findByEndDateGreaterThanEqualAndResultsChecked(Long from, Boolean resultsChecked);

    List<MatchDay> findByEndDateLessThanEqualAndResultsChecked(Long to, Boolean resultsChecked);

    List<MatchDay> findByEndDateGreaterThanEqual(Long from);

    List<MatchDay> findByEndDateLessThanEqual(Long to);

    List<MatchDay> findByResultsChecked(Boolean resultsChecked);

}



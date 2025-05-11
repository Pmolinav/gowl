package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.LeagueCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueCategoryRepository extends JpaRepository<LeagueCategory, String> {
}



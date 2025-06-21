package com.pmolinav.matchdatasync.repositories;

import com.pmolinav.predictionslib.model.Odds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OddsRepository extends JpaRepository<Odds, Long> {

}



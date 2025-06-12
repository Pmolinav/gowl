package com.pmolinav.predictions.repositories;

import com.pmolinav.predictionslib.model.Odds;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OddsRepository extends JpaRepository<Odds, Long> {

    List<Odds> findByEventId(Long eventId);
}



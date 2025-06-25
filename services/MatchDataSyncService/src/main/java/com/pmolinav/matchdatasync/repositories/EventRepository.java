package com.pmolinav.matchdatasync.repositories;

import com.pmolinav.predictionslib.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findByMatchId(Long matchId);
}



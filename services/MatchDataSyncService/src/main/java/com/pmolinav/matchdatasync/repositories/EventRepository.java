package com.pmolinav.matchdatasync.repositories;

import com.pmolinav.predictionslib.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}



package com.pmolinav.bookings.repository;

import com.pmolinav.userslib.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
    // All crud database methods
}



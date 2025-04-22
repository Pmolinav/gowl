package net.pmolinav.bookings.repository;

import net.pmolinav.bookingslib.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
    // All crud database methods
}



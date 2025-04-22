package net.pmolinav.bookings.repository;

import net.pmolinav.bookingslib.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // All crud database methods

}



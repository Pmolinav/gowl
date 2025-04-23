package com.pmolinav.bookings.repository;

import com.pmolinav.userslib.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // All crud database methods

}



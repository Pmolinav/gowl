package com.pmolinav.bookings.repository;

import com.pmolinav.userslib.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, String> {
    // All crud database methods

}



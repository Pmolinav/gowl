package com.pmolinav.users.repositories;

import com.pmolinav.userslib.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // All crud database methods
    Optional<User> findByUsername(String username);
}



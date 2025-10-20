package com.pmolinav.users.repositories;

import com.pmolinav.userslib.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    // All crud database methods
    Optional<Token> findByRefreshToken(String refreshToken);

    void deleteByUsername(String username);

    void deleteByUsernameAndRefreshToken(String username, String refreshToken);
}



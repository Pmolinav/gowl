package com.pmolinav.matchdatasync.repositories;

import com.pmolinav.predictionslib.model.PlayerBet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerBetRepository extends JpaRepository<PlayerBet, Long> {

    @Override
    @EntityGraph(attributePaths = "selections")
    Optional<PlayerBet> findById(Long aLong);

    @EntityGraph(attributePaths = "selections")
    List<PlayerBet> findByMatchId(Long matchId);

    @EntityGraph(attributePaths = "selections")
    List<PlayerBet> findByUsername(String username);

    @EntityGraph(attributePaths = "selections")
    Page<PlayerBet> findByMatchId(Long matchId, Pageable pageable);

}
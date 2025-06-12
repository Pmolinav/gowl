package com.pmolinav.predictions.repositories;

import com.pmolinav.predictionslib.model.PlayerBet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerBetRepository extends JpaRepository<PlayerBet, Long> {

    List<PlayerBet> findByMatchId(Long matchId);

    List<PlayerBet> findByUsername(String username);
}



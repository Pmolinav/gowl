package com.pmolinav.predictions.repositories;

import com.pmolinav.predictionslib.model.PlayerBetSelection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerBetSelectionRepository extends JpaRepository<PlayerBetSelection, Long> {

    List<PlayerBetSelection> findByBetId(Long betId);

    List<PlayerBetSelection> findByOddsId(Long oddsId);

    List<PlayerBetSelection> findByBetIdAndOddsId(Long betId, Long oddsId);

}



package com.pmolinav.predictions.repositories;

import com.pmolinav.predictionslib.model.PlayerBet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerBetRepository extends JpaRepository<PlayerBet, Long> {

    @Override
    @EntityGraph(attributePaths = "selections")
    List<PlayerBet> findAll();

    @Override
    @EntityGraph(attributePaths = "selections")
    Optional<PlayerBet> findById(Long aLong);

    @EntityGraph(attributePaths = "selections")
    List<PlayerBet> findByMatchId(Long matchId);

    @EntityGraph(attributePaths = "selections")
    List<PlayerBet> findByUsername(String username);

    @Query("""
                SELECT DISTINCT pb 
                FROM PlayerBet pb
                JOIN FETCH pb.match m
                LEFT JOIN FETCH pb.selections s
                LEFT JOIN FETCH s.odds o
                WHERE pb.username = :username
            """)
    List<PlayerBet> findAllByUsernameWithDetails(@Param("username") String username);
}



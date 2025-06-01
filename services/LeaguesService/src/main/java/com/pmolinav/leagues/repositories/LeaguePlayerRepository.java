package com.pmolinav.leagues.repositories;

import com.pmolinav.leagueslib.model.LeaguePlayer;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LeaguePlayerRepository extends JpaRepository<LeaguePlayer, LeaguePlayerId> {

    List<LeaguePlayer> findByLeagueId(Long leagueId);

    List<LeaguePlayer> findByUsername(String username);

    @Modifying
    @Query("UPDATE LeaguePlayer lp SET lp.totalPoints = lp.totalPoints + :points " +
            "WHERE lp.id.leagueId = :leagueId AND lp.id.username = :username")
    int addPointsToLeaguePlayer(@Param("leagueId") Long leagueId,
                                @Param("username") String username,
                                @Param("points") Integer points);
}



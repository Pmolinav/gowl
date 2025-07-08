package com.pmolinav.league.services;

import com.pmolinav.league.clients.LeaguePlayersClient;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import feign.FeignException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaguePlayersService {

    private static final Logger logger = LoggerFactory.getLogger(LeaguePlayersService.class);

    @Autowired
    private LeaguePlayersClient leaguePlayersClient;

    public LeaguePlayerDTO findLeaguePlayerByByLeagueIdAndPlayer(long id, String username) {
        try {
            return leaguePlayersClient.findLeaguePlayerByLeagueIdAndPlayer(id, username);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League players not found by leagueId {} and username {}.", id, username, e);
                throw new NotFoundException("League players not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<LeaguePlayerDTO> findLeaguePlayersByLeagueId(long id) {
        try {
            return leaguePlayersClient.findLeaguePlayersByLeagueId(id);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League players found.", e);
                throw new NotFoundException("League players not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<LeagueDTO> findLeaguesByUsername(String username) {
        try {
            return leaguePlayersClient.findLeaguesByUsername(username);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Leagues not found.", e);
                throw new NotFoundException("Leagues not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<LeaguePlayerId> createLeaguePlayers(List<LeaguePlayerDTO> leaguePlayers) {
        try {
            return leaguePlayersClient.createLeaguePlayers(leaguePlayers);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void addPointsToLeaguePlayer(long id, String username, int points) {
        try {
            leaguePlayersClient.addPointsToLeaguePlayer(id, username, points);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League players not found by leagueId {} and username {}.", id, username, e);
                throw new NotFoundException("League players not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteLeaguePlayersByLeagueIdAndPlayer(Long id, String username) {
        try {
            leaguePlayersClient.deleteLeaguePlayersByLeagueIdAndPlayer(id, username);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League players not found by leagueId {} and username {}.", id, username, e);
                throw new NotFoundException("League players not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteLeaguePlayersByLeagueId(Long id) {
        try {
            leaguePlayersClient.deleteLeaguePlayersByLeagueId(id);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League players not found by leagueId {}.", id, e);
                throw new NotFoundException("League players not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

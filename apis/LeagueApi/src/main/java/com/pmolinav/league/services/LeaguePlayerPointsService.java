package com.pmolinav.league.services;

import com.pmolinav.league.clients.LeaguePlayerPointsClient;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import feign.FeignException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaguePlayerPointsService {

    private static final Logger logger = LoggerFactory.getLogger(LeaguePlayerPointsService.class);

    @Autowired
    private LeaguePlayerPointsClient leaguePlayerPointsClient;

    public List<LeaguePlayerPointsDTO> findLeaguePlayerPointsByLeagueIdAndPlayer(long id, String username) {
        try {
            return leaguePlayerPointsClient.findLeaguePlayerPointsByLeagueIdAndPlayer(id, username);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League player points not found by leagueId {} and username {}.", id, username, e);
                throw new NotFoundException("League player points not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<LeaguePlayerPointsDTO> findLeaguePlayerPointsByCategorySeasonAndNumber(String categoryId, int season, int number) {
        try {
            return leaguePlayerPointsClient.findLeaguePlayerPointsByCategorySeasonAndNumber(categoryId, season, number);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League player points not found by categoryId {}, season {} and number {}.",
                        categoryId, season, number, e);
                throw new NotFoundException("League player points not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public LeaguePlayerPointsDTO createOrUpdateLeaguePlayerPoints(LeaguePlayerPointsDTO leaguePlayerPoints) {
        try {
            return leaguePlayerPointsClient.createOrUpdateLeaguePlayerPoints(leaguePlayerPoints);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteLeaguePlayerPointsByLeagueIdAndPlayer(long id, String username) {
        try {
            leaguePlayerPointsClient.deleteLeaguePlayerPointsByLeagueIdAndPlayer(id, username);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League player points not found by leagueId {} and username {}.", id, username, e);
                throw new NotFoundException("League player points not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteLeaguePlayerByCategorySeasonAndNumber(String categoryId, int season, int number) {
        try {
            leaguePlayerPointsClient.deleteLeaguePlayerByCategorySeasonAndNumber(categoryId, season, number);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League players not found by categoryId {}, season {} and number {}.",
                        categoryId, season, number, e);
                throw new NotFoundException("League player points not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}

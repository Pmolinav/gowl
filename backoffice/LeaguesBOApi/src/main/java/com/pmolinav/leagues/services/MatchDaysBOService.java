package com.pmolinav.leagues.services;

import com.pmolinav.leagues.clients.LeaguesServiceClient;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.model.MatchDayId;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import feign.FeignException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchDaysBOService {

    private static final Logger logger = LoggerFactory.getLogger(MatchDaysBOService.class);

    @Autowired
    private LeaguesServiceClient leaguesServiceClient;

    public List<MatchDayDTO> findAllMatchDays() {
        try {
            return leaguesServiceClient.findAllMatchDays();
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Match days not found.", e);
                throw new NotFoundException("Match days not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<MatchDayDTO> findMatchDayByCategoryId(String categoryId) {
        try {
            return leaguesServiceClient.findMatchDayByCategoryId(categoryId);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Match days not found by categoryId {}.", categoryId, e);
                throw new NotFoundException("Match days not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<MatchDayDTO> findMatchDayByCategoryIdAndSeason(String categoryId, int season) {
        try {
            return leaguesServiceClient.findMatchDayByCategoryIdAndSeason(categoryId, season);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Match days not found by categoryId {} and season {}.", categoryId, season, e);
                throw new NotFoundException("Match days not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public MatchDayId createMatchDay(MatchDayDTO matchDay) {
        try {
            return leaguesServiceClient.createMatchDay(matchDay);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<MatchDayId> createMatchDays(List<MatchDayDTO> matchDays) {
        try {
            return leaguesServiceClient.createMatchDays(matchDays);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteMatchDaysByCategoryId(String categoryId) {
        try {
            leaguesServiceClient.deleteMatchDaysByCategoryId(categoryId);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Match days not found by categoryId {}.", categoryId, e);
                throw new NotFoundException("Match days not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteMatchDaysByCategoryIdAndSeason(String categoryId, int season) {
        try {
            leaguesServiceClient.deleteMatchDaysByCategoryIdAndSeason(categoryId, season);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Match days not found by categoryId {} and season {}.", categoryId, season, e);
                throw new NotFoundException("Match days not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteMatchDayByCategoryIdSeasonAndNumber(String categoryId, int season, int number) {
        try {
            leaguesServiceClient.deleteMatchDayByCategoryIdSeasonAndNumber(categoryId, season, number);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Match days not found by categoryId {}, season {} and number {}.",
                        categoryId, season, number, e);
                throw new NotFoundException("Match days not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

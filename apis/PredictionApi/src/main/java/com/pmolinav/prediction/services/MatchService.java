package com.pmolinav.prediction.services;

import com.pmolinav.prediction.clients.MatchClient;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.MatchDTO;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    @Autowired
    private MatchClient matchClient;

    public MatchDTO findMatchById(Long id) {
        try {
            return matchClient.findById(id);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling Match service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Match with id {} not found.", id, e);
                throw new NotFoundException("Match " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling Match service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<MatchDTO> findByCategoryIdAndSeasonAndMatchDayNumber(String categoryId,
                                                                     Integer season,
                                                                     Integer matchDayNumber) {
        try {
            return matchClient.findByCategoryIdSeasonAndMatchDayNumber(categoryId, season, matchDayNumber);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling Match service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Matches by categoryId {}, season {} and matchDayNumber {} not found.",
                        categoryId, season, matchDayNumber, e);
                throw new NotFoundException("Matches not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling Match service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}
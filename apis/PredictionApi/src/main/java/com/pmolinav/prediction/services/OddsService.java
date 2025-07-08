package com.pmolinav.prediction.services;

import com.pmolinav.prediction.clients.OddsClient;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.OddsDTO;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OddsService {

    private static final Logger logger = LoggerFactory.getLogger(OddsService.class);

    @Autowired
    private OddsClient oddsClient;

    public OddsDTO findById(Long id) {
        try {
            return oddsClient.findById(id);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Odds with id {} not found.", id, e);
                throw new NotFoundException("Odds with id " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected error while fetching odds by id", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<OddsDTO> findByEventType(String type) {
        try {
            return oddsClient.findByEventType(type);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Odds with type {} not found.", type, e);
                throw new NotFoundException("Odds with type " + type + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected error while fetching odds by eventType", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<OddsDTO> findOddsByMatchId(Long matchId) {
        try {
            return oddsClient.findByMatchId(matchId);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Odds with match id {} not found.", matchId, e);
                throw new NotFoundException("Odds by matchId " + matchId + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}
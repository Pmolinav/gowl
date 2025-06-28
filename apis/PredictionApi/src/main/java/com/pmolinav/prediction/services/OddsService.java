package com.pmolinav.prediction.services;

import com.pmolinav.prediction.clients.OddsClient;
import com.pmolinav.prediction.exceptions.InternalServerErrorException;
import com.pmolinav.prediction.exceptions.NotFoundException;
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

    public List<OddsDTO> findAll() {
        try {
            return oddsClient.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while fetching all odds", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public OddsDTO findById(Long id) {
        try {
            return oddsClient.findById(id);
        } catch (FeignException.NotFound e) {
            logger.warn("Odds with id {} not found", id);
            throw new NotFoundException("Odds with id " + id + " not found");
        } catch (Exception e) {
            logger.error("Unexpected error while fetching odds by id", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<OddsDTO> findByEventType(String type) {
        try {
            return oddsClient.findByEventType(type);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching odds by eventType", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Long create(OddsDTO dto) {
        try {
            return oddsClient.create(dto);
        } catch (Exception e) {
            logger.error("Error while creating odds", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void update(Long id, OddsDTO dto) {
        try {
            oddsClient.update(id, dto);
        } catch (FeignException.NotFound e) {
            logger.warn("Odds with id {} not found for update", id);
            throw new NotFoundException("Odds with id " + id + " not found");
        } catch (Exception e) {
            logger.error("Error while updating odds", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            oddsClient.delete(id);
        } catch (FeignException.NotFound e) {
            logger.warn("Odds with id {} not found for delete", id);
            throw new NotFoundException("Odds with id " + id + " not found");
        } catch (Exception e) {
            logger.error("Error while deleting odds", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}
package com.pmolinav.prediction.services;

import com.pmolinav.prediction.clients.PlayerBetSelectionClient;
import com.pmolinav.prediction.exceptions.InternalServerErrorException;
import com.pmolinav.prediction.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerBetSelectionService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBetSelectionService.class);

    @Autowired
    private PlayerBetSelectionClient playerBetSelectionClient;

    public List<PlayerBetSelectionDTO> findAll() {
        try {
            return playerBetSelectionClient.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while fetching all selections", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public PlayerBetSelectionDTO findById(Long id) {
        try {
            return playerBetSelectionClient.findById(id);
        } catch (FeignException.NotFound e) {
            logger.warn("Selection with id {} not found", id);
            throw new NotFoundException("Selection with id " + id + " not found");
        } catch (Exception e) {
            logger.error("Unexpected error while fetching selection by id", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<PlayerBetSelectionDTO> findByBetId(Long betId) {
        try {
            return playerBetSelectionClient.findByBetId(betId);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching selections by betId", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Long create(PlayerBetSelectionDTO dto) {
        try {
            return playerBetSelectionClient.create(dto);
        } catch (Exception e) {
            logger.error("Error while creating selection", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            playerBetSelectionClient.delete(id);
        } catch (FeignException.NotFound e) {
            logger.warn("Selection with id {} not found for delete", id);
            throw new NotFoundException("Selection with id " + id + " not found");
        } catch (Exception e) {
            logger.error("Error while deleting selection", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

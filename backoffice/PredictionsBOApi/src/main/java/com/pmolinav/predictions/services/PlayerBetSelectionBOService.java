package com.pmolinav.predictions.services;

import com.pmolinav.predictions.clients.PredictionsServiceClient;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerBetSelectionBOService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBetSelectionBOService.class);

    @Autowired
    private PredictionsServiceClient predictionsServiceClient;

    public List<PlayerBetSelectionDTO> findAll() {
        try {
            return predictionsServiceClient.findAllPlayerBetSelections();
        } catch (Exception e) {
            logger.error("Unexpected error while fetching all selections", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public PlayerBetSelectionDTO findById(Long id) {
        try {
            return predictionsServiceClient.findPlayerBetSelectionById(id);
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
            return predictionsServiceClient.findPlayerBetSelectionsByBetId(betId);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching selections by betId", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Long create(PlayerBetSelectionDTO dto) {
        try {
            return predictionsServiceClient.createPlayerBetSelection(dto);
        } catch (Exception e) {
            logger.error("Error while creating selection", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            predictionsServiceClient.deletePlayerBetSelection(id);
        } catch (FeignException.NotFound e) {
            logger.warn("Selection with id {} not found for delete", id);
            throw new NotFoundException("Selection with id " + id + " not found");
        } catch (Exception e) {
            logger.error("Error while deleting selection", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

package com.pmolinav.prediction.services;

import com.pmolinav.prediction.clients.PredictionsServiceClient;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerBetService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBetService.class);

    @Autowired
    private PredictionsServiceClient predictionsServiceClient;

    public PlayerBetDTO findById(Long id) {
        try {
            return predictionsServiceClient.findPlayerBetById(id);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("PlayerBet with id {} not found.", id, e);
                throw new NotFoundException("PlayerBet with id " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected error while fetching player bet by id", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<PlayerBetDTO> findByMatchId(Long matchId) {
        try {
            return predictionsServiceClient.findPlayerBetsByMatchId(matchId);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("PlayerBet by match id {} not found.", matchId, e);
                throw new NotFoundException("PlayerBet by match id " + matchId + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected error while fetching player bets by matchId", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<PlayerBetDTO> findByUsername(String username) {
        try {
            return predictionsServiceClient.findPlayerBetsByUsername(username);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("PlayerBet by username {} not found.", username, e);
                throw new NotFoundException("PlayerBet by username " + username + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected error while fetching player bets by username", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Long create(PlayerBetDTO dto) {
        try {
            return predictionsServiceClient.create(dto);
        } catch (Exception e) {
            logger.error("Error while creating player bet", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            predictionsServiceClient.delete(id);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("PlayerBet with id {} not found.", id, e);
                throw new NotFoundException("PlayerBet with id " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Error while deleting player bet", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
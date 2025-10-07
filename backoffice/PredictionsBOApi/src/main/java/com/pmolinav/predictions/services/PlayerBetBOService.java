package com.pmolinav.predictions.services;

import com.pmolinav.predictions.clients.PlayerBetClient;
import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.PlayerBetByUsernameDTO;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerBetBOService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBetBOService.class);

    @Autowired
    private PlayerBetClient playerBetClient;

    public List<PlayerBetDTO> findAll() {
        try {
            return playerBetClient.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while fetching all player bets", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public PlayerBetDTO findById(Long id) {
        try {
            return playerBetClient.findById(id);
        } catch (FeignException.NotFound e) {
            logger.warn("PlayerBet with id {} not found", id);
            throw new NotFoundException("PlayerBet with id " + id + " not found");
        } catch (Exception e) {
            logger.error("Unexpected error while fetching player bet by id", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<PlayerBetDTO> findByMatchId(Long matchId) {
        try {
            return playerBetClient.findByMatchId(matchId);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching player bets by matchId", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<PlayerBetByUsernameDTO> findByUsername(String username) {
        try {
            return playerBetClient.findByUsername(username);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching player bets by username", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Long create(PlayerBetDTO dto) {
        try {
            return playerBetClient.create(dto);
        } catch (Exception e) {
            logger.error("Error while creating player bet", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            playerBetClient.delete(id);
        } catch (FeignException.NotFound e) {
            logger.warn("PlayerBet with id {} not found for delete", id);
            throw new NotFoundException("PlayerBet with id " + id + " not found");
        } catch (Exception e) {
            logger.error("Error while deleting player bet", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
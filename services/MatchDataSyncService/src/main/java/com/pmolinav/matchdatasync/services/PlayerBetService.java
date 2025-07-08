package com.pmolinav.matchdatasync.services;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.matchdatasync.repositories.PlayerBetRepository;
import com.pmolinav.matchdatasync.repositories.PlayerBetSelectionRepository;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.predictionslib.mapper.PlayerBetMapper;
import com.pmolinav.predictionslib.model.PlayerBet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@EnableAsync
@Service
public class PlayerBetService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBetService.class);

    private final PlayerBetRepository playerBetRepository;
    private final PlayerBetSelectionRepository playerBetSelectionRepository;
    private final PlayerBetMapper playerBetMapper;

    @Autowired
    public PlayerBetService(PlayerBetRepository playerBetRepository,
                            PlayerBetSelectionRepository playerBetSelectionRepository,
                            PlayerBetMapper playerBetMapper) {
        this.playerBetRepository = playerBetRepository;
        this.playerBetSelectionRepository = playerBetSelectionRepository;
        this.playerBetMapper = playerBetMapper;
    }

    @Transactional(readOnly = true)
    public PlayerBetDTO findById(Long id) {
        try {
            PlayerBet bet = playerBetRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Player bet not found with id: " + id));
            return playerBetMapper.playerBetEntityToDto(bet);
        } catch (NotFoundException e) {
            logger.error("Player bet not found by id: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching player bet by id {}", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<PlayerBetDTO> findByMatchId(Long matchId) {
        try {
            List<PlayerBet> bets = playerBetRepository.findByMatchId(matchId);
            if (CollectionUtils.isEmpty(bets)) {
                logger.warn("No player bets found for matchId {}", matchId);
                throw new NotFoundException("No player bets for matchId: " + matchId);
            }
            return bets.stream().map(playerBetMapper::playerBetEntityToDto).toList();
        } catch (Exception e) {
            logger.error("Error fetching player bets by matchId {}", matchId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<PlayerBetDTO> findByUsername(String username) {
        try {
            List<PlayerBet> bets = playerBetRepository.findByUsername(username);
            if (CollectionUtils.isEmpty(bets)) {
                logger.warn("No player bets found for username {}", username);
                throw new NotFoundException("No player bets for username: " + username);
            }
            return bets.stream().map(playerBetMapper::playerBetEntityToDto).toList();
        } catch (Exception e) {
            logger.error("Error fetching player bets by username {}", username, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }


    @Transactional(readOnly = true)
    public Page<PlayerBet> findPendingBetsByMatchId(Long matchId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return playerBetRepository.findByMatchId(matchId, pageable);
    }

    @Transactional
    public void saveAll(List<PlayerBet> bets) {
        playerBetRepository.saveAll(bets);
    }

}

package com.pmolinav.predictions.services;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.predictions.repositories.PlayerBetRepository;
import com.pmolinav.predictions.repositories.PlayerBetSelectionRepository;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.predictionslib.mapper.PlayerBetMapper;
import com.pmolinav.predictionslib.model.PlayerBet;
import com.pmolinav.predictionslib.model.PlayerBetSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public PlayerBetService(PlayerBetRepository playerBetRepository,
                            PlayerBetSelectionRepository playerBetSelectionRepository,
                            PlayerBetMapper playerBetMapper) {
        this.playerBetRepository = playerBetRepository;
        this.playerBetSelectionRepository = playerBetSelectionRepository;
        this.playerBetMapper = playerBetMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<PlayerBetDTO> findAll() {
        List<PlayerBet> bets;
        try {
            bets = playerBetRepository.findAll();
        } catch (Exception e) {
            logger.error("Error fetching all player bets.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(bets)) {
            logger.warn("No player bets found.");
            throw new NotFoundException("No player bets found.");
        }
        return bets.stream().map(playerBetMapper::playerBetEntityToDto).toList();
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

    @Transactional
    public PlayerBet create(PlayerBetDTO dto) {
        try {
            PlayerBet bet = playerBetMapper.playerBetDtoToEntity(dto);
            PlayerBet storedPlayerBet = playerBetRepository.save(bet);

            List<PlayerBetSelection> selections = bet.getSelections();
            selections.forEach(playerBetSelection ->
                    playerBetSelection.setBetId(storedPlayerBet.getBetId()));

            List<PlayerBetSelection> storedSelections = playerBetSelectionRepository.saveAll(selections);
            storedPlayerBet.setSelections(storedSelections);

            return storedPlayerBet;
        } catch (Exception e) {
            logger.error("Error creating player bet.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            List<PlayerBetSelection> selections = playerBetSelectionRepository.findByBetId(id);
            playerBetSelectionRepository.deleteAll(selections);

            PlayerBet bet = playerBetRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Player bet not found with id: " + id));
            playerBetRepository.delete(bet);
        } catch (NotFoundException e) {
            logger.error("Player bet not found by id: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting player bet by id {}", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteByMatchId(Long matchId) {
        try {
            List<PlayerBet> bets = playerBetRepository.findByMatchId(matchId);
            if (CollectionUtils.isEmpty(bets)) {
                throw new NotFoundException("No player bets found for matchId: " + matchId);
            }
            playerBetRepository.deleteAll(bets);
        } catch (Exception e) {
            logger.error("Error deleting player bets by matchId {}", matchId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteByUsername(String username) {
        try {
            List<PlayerBet> bets = playerBetRepository.findByUsername(username);
            if (CollectionUtils.isEmpty(bets)) {
                throw new NotFoundException("No player bets found for username: " + username);
            }
            playerBetRepository.deleteAll(bets);
        } catch (Exception e) {
            logger.error("Error deleting player bets by username {}", username, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

//    @Async
//    public void storeInKafka(ChangeType changeType, Long leagueId, League league) {
//        try {
//            messageProducer.sendMessage(this.KAFKA_TOPIC, new History(
//                    new Date(),
//                    changeType,
//                    "League",
//                    String.valueOf(leagueId),
//                    league == null ? null : new ObjectMapper().writeValueAsString(league), // TODO: USE JSON PATCH.
//                    "Admin" // TODO: createLeague is not implemented yet.
//            ));
//        } catch (Exception e) {
//            logger.warn("Kafka operation {} with name {} and league {} need to be reviewed", changeType, leagueId, league);
//        }
//    }
}

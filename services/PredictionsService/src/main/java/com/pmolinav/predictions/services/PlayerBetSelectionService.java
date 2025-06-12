package com.pmolinav.predictions.services;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.repositories.PlayerBetSelectionRepository;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
import com.pmolinav.predictionslib.mapper.PlayerBetSelectionMapper;
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
public class PlayerBetSelectionService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBetSelectionService.class);

    private final PlayerBetSelectionRepository playerBetSelectionRepository;
    private final PlayerBetSelectionMapper playerBetSelectionMapper;
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public PlayerBetSelectionService(PlayerBetSelectionRepository playerBetSelectionRepository,
                                     PlayerBetSelectionMapper playerBetSelectionMapper) {
        this.playerBetSelectionRepository = playerBetSelectionRepository;
        this.playerBetSelectionMapper = playerBetSelectionMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<PlayerBetSelectionDTO> findAll() {
        List<PlayerBetSelection> selections;
        try {
            selections = playerBetSelectionRepository.findAll();
        } catch (Exception e) {
            logger.error("Error fetching all selections", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(selections)) {
            throw new NotFoundException("No selections found");
        }
        return selections.stream().map(playerBetSelectionMapper::playerBetSelectionEntityToDto).toList();
    }

    @Transactional(readOnly = true)
    public PlayerBetSelectionDTO findById(Long id) {
        try {
            return playerBetSelectionMapper.playerBetSelectionEntityToDto(playerBetSelectionRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Selection not found")));
        } catch (NotFoundException e) {
            logger.error("Selection not found by id: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching selection by id {}", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public PlayerBetSelection create(PlayerBetSelectionDTO dto) {
        try {
            PlayerBetSelection selection = playerBetSelectionMapper.playerBetSelectionDtoToEntity(dto);
            return playerBetSelectionRepository.save(selection);
        } catch (Exception e) {
            logger.error("Error creating selection", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            PlayerBetSelection selection = playerBetSelectionRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Selection not found"));
            playerBetSelectionRepository.delete(selection);
        } catch (NotFoundException e) {
            logger.error("Selection not found by id: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting selection by id {}", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<PlayerBetSelectionDTO> findByBetId(Long betId) {
        try {
            List<PlayerBetSelection> list = playerBetSelectionRepository.findByBetId(betId);
            if (list.isEmpty()) throw new NotFoundException("No selections for betId");
            return list.stream().map(playerBetSelectionMapper::playerBetSelectionEntityToDto).toList();
        } catch (Exception e) {
            logger.error("Error fetching by betId {}", betId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<PlayerBetSelectionDTO> findByOddId(Long oddId) {
        try {
            List<PlayerBetSelection> list = playerBetSelectionRepository.findByOddsId(oddId);
            if (list.isEmpty()) throw new NotFoundException("No selections for oddId");
            return list.stream().map(playerBetSelectionMapper::playerBetSelectionEntityToDto).toList();
        } catch (Exception e) {
            logger.error("Error fetching by oddId {}", oddId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<PlayerBetSelectionDTO> findByBetIdAndOddId(Long betId, Long oddId) {
        try {
            List<PlayerBetSelection> list = playerBetSelectionRepository.findByBetIdAndOddsId(betId, oddId);
            if (list.isEmpty()) throw new NotFoundException("No selections for betId and oddId");
            return list.stream().map(playerBetSelectionMapper::playerBetSelectionEntityToDto).toList();
        } catch (Exception e) {
            logger.error("Error fetching by betId {} and oddId {}", betId, oddId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteByBetId(Long betId) {
        try {
            List<PlayerBetSelection> list = playerBetSelectionRepository.findByBetId(betId);
            if (list.isEmpty()) throw new NotFoundException("No selections to delete for betId");
            playerBetSelectionRepository.deleteAll(list);
        } catch (Exception e) {
            logger.error("Error deleting by betId {}", betId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteByOddId(Long oddId) {
        try {
            List<PlayerBetSelection> list = playerBetSelectionRepository.findByOddsId(oddId);
            if (list.isEmpty()) throw new NotFoundException("No selections to delete for oddId");
            playerBetSelectionRepository.deleteAll(list);
        } catch (Exception e) {
            logger.error("Error deleting by oddId {}", oddId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteByBetIdAndOddId(Long betId, Long oddId) {
        try {
            List<PlayerBetSelection> list = playerBetSelectionRepository.findByBetIdAndOddsId(betId, oddId);
            if (list.isEmpty()) throw new NotFoundException("No selections to delete for betId and oddId");
            playerBetSelectionRepository.deleteAll(list);
        } catch (Exception e) {
            logger.error("Error deleting by betId {} and oddId {}", betId, oddId, e);
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

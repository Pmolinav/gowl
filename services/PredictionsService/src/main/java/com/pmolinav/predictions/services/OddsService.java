package com.pmolinav.predictions.services;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.repositories.OddsRepository;
import com.pmolinav.predictionslib.dto.OddsDTO;
import com.pmolinav.predictionslib.mapper.OddsMapper;
import com.pmolinav.predictionslib.model.Odds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@EnableAsync
@Service
public class OddsService {

    private static final Logger logger = LoggerFactory.getLogger(OddsService.class);

    private final OddsRepository oddsRepository;
    private final OddsMapper oddsMapper;
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public OddsService(OddsRepository oddsRepository, OddsMapper oddsMapper) {
        this.oddsRepository = oddsRepository;
        this.oddsMapper = oddsMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<OddsDTO> findAllOdds() {
        List<Odds> oddsList;
        try {
            oddsList = oddsRepository.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving all odds.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(oddsList)) {
            logger.warn("No odds found.");
            throw new NotFoundException("No odds found.");
        }
        return oddsList.stream().map(oddsMapper::oddsEntityToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OddsDTO findOddsById(Long oddsId) {
        try {
            Odds odds = oddsRepository.findById(oddsId)
                    .orElseThrow(() -> new NotFoundException("Odds with id " + oddsId + " not found."));
            return oddsMapper.oddsEntityToDto(odds);
        } catch (NotFoundException e) {
            logger.error("Odds not found with id: {}", oddsId);
            throw e;
        } catch (Exception e) {
            logger.error("Error while retrieving odds by id {}", oddsId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<OddsDTO> findOddsByEventType(String eventType) {
        List<Odds> oddsList;
        try {
            oddsList = oddsRepository.findByEventType(eventType);
        } catch (Exception e) {
            logger.error("Error while retrieving odds by eventType {}", eventType, e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(oddsList)) {
            logger.warn("No odds found for eventType {}", eventType);
            throw new NotFoundException("No odds found for provided eventType.");
        }
        return oddsList.stream().map(oddsMapper::oddsEntityToDto).collect(Collectors.toList());
    }

    @Transactional
    public Odds createOdds(OddsDTO oddsDTO) {
        try {
            Odds odds = oddsMapper.oddsDtoToEntity(oddsDTO);
            return oddsRepository.save(odds);
        } catch (Exception e) {
            logger.error("Error while creating new odds.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void updateOdds(Long oddsId, OddsDTO oddsDTO) {
        try {
            Odds existing = oddsRepository.findById(oddsId)
                    .orElseThrow(() -> new NotFoundException("Odds with id " + oddsId + " not found."));
            Odds updated = oddsMapper.oddsDtoToEntity(oddsDTO);
            updated.setOddsId(existing.getOddsId()); // ensure ID is preserved
            oddsRepository.save(updated);
        } catch (NotFoundException e) {
            logger.error("Odds not found with id: {}", oddsId);
            throw e;
        } catch (Exception e) {
            logger.error("Error while updating odds with id {}", oddsId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteOdds(Long oddsId) {
        try {
            Odds odds = oddsRepository.findById(oddsId)
                    .orElseThrow(() -> new NotFoundException("Odds with id " + oddsId + " not found."));
            oddsRepository.delete(odds);
        } catch (NotFoundException e) {
            logger.error("Odds not found by id: {}", oddsId);
            throw e;
        } catch (Exception e) {
            logger.error("Error while deleting odds with id {}", oddsId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

//    @Async
//    public void storeInKafka(ChangeType changeType, Long oddsId, Odds odds) {
//        try {
//            messageProducer.sendMessage(this.KAFKA_TOPIC, new History(
//                    new Date(),
//                    changeType,
//                    "Odds",
//                    String.valueOf(oddsId),
//                    odds == null ? null : new ObjectMapper().writeValueAsString(odds), // TODO: USE JSON PATCH.
//                    "Admin" // TODO: createOdds is not implemented yet.
//            ));
//        } catch (Exception e) {
//            logger.warn("Kafka operation {} with name {} and league category {} need to be reviewed", changeType, oddsId, odds);
//        }
//    }

}

package com.pmolinav.matchdatasync.services;

import com.pmolinav.matchdatasync.exceptions.InternalServerErrorException;
import com.pmolinav.matchdatasync.exceptions.NotFoundException;
import com.pmolinav.matchdatasync.repositories.OddsRepository;
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

@EnableAsync
@Service
public class OddsService {

    private static final Logger logger = LoggerFactory.getLogger(OddsService.class);

    private final OddsRepository oddsRepository;
    private final OddsMapper oddsMapper;

    @Autowired
    public OddsService(OddsRepository oddsRepository, OddsMapper oddsMapper) {
        this.oddsRepository = oddsRepository;
        this.oddsMapper = oddsMapper;
    }

    @Transactional(readOnly = true)
    public Odds findOddsById(Long oddsId) {
        try {
            return oddsRepository.findById(oddsId)
                    .orElseThrow(() -> new NotFoundException("Odds with id " + oddsId + " not found."));
        } catch (NotFoundException e) {
            logger.error("Odds not found with id: {}", oddsId);
            throw e;
        } catch (Exception e) {
            logger.error("Error while retrieving odds by id {}", oddsId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Odds> findOddsByEventType(String type) {
        List<Odds> oddsList;
        try {
            oddsList = oddsRepository.findByEventType(type);
            if (CollectionUtils.isEmpty(oddsList)) {
                logger.warn("No odds found for eventType {}", type);
                throw new NotFoundException("No odds found for provided eventType.");
            }
            return oddsList;
        } catch (Exception e) {
            logger.error("Error while retrieving odds by eventType {}", type, e);
            throw new InternalServerErrorException(e.getMessage());
        }
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
    public List<Odds> createOddsList(List<OddsDTO> oddsDTOList) {
        try {
            List<Odds> oddsList = oddsDTOList.stream().map(oddsMapper::oddsDtoToEntity).toList();
            return oddsRepository.saveAll(oddsList);
        } catch (Exception e) {
            logger.error("Error while creating new odds list.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}

package com.pmolinav.matchdatasync.services;

import com.pmolinav.matchdatasync.exceptions.InternalServerErrorException;
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

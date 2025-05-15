package com.pmolinav.leagues.services;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.repositories.MatchDayRepository;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.mapper.MatchDayMapper;
import com.pmolinav.leagueslib.model.MatchDay;
import com.pmolinav.leagueslib.model.MatchDayId;
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
public class MatchDayService {

    private static final Logger logger = LoggerFactory.getLogger(MatchDayService.class);

    private final MatchDayRepository matchDayRepository;
    private final MatchDayMapper matchDayMapper;
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public MatchDayService(MatchDayRepository matchDayRepository, MatchDayMapper matchDayMapper) {
        this.matchDayRepository = matchDayRepository;
        this.matchDayMapper = matchDayMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<MatchDayDTO> findAllMatchDays() {
        List<MatchDay> matchDaysList;
        try {
            matchDaysList = matchDayRepository.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while searching all match days in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(matchDaysList)) {
            logger.warn("No match days were found in repository.");
            throw new NotFoundException("No match days found in repository.");
        } else {
            return matchDaysList.stream()
                    .map(matchDayMapper::matchDayEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public MatchDay createMatchDay(MatchDayDTO matchDayDTO) {
        try {
            MatchDay matchDay = matchDayMapper.matchDayDtoToEntity(matchDayDTO);
            return matchDayRepository.save(matchDay);
        } catch (Exception e) {
            logger.error("Unexpected error while creating new match day in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<MatchDayDTO> findByCategoryId(String categoryId) {
        List<MatchDay> matchDaysList;
        try {
            matchDaysList = matchDayRepository.findByCategoryId(categoryId);
        } catch (Exception e) {
            logger.error("Unexpected error while searching match days by categoryId {} in repository.", categoryId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(matchDaysList)) {
            logger.warn("No match days were found by categoryId {} in repository.", categoryId);
            throw new NotFoundException("No match days found in repository by requested categoryId.");
        } else {
            return matchDaysList.stream()
                    .map(matchDayMapper::matchDayEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional(readOnly = true)
    public List<MatchDayDTO> findByCategoryIdAndSeason(String categoryId, Integer season) {
        List<MatchDay> matchDaysList;
        try {
            matchDaysList = matchDayRepository.findByCategoryIdAndSeason(categoryId, season);
        } catch (Exception e) {
            logger.error("Unexpected error while searching match days by categoryId {} and season {} in repository.",
                    categoryId, season, e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(matchDaysList)) {
            logger.warn("No match days were found by categoryId {} and season {} in repository.", categoryId, season);
            throw new NotFoundException("No match days found in repository by requested categoryId and season.");
        } else {
            return matchDaysList.stream()
                    .map(matchDayMapper::matchDayEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public void deleteMatchDayByCategoryId(String categoryId) {
        try {
            List<MatchDay> matchDaysList = matchDayRepository.findByCategoryId(categoryId);
            if (CollectionUtils.isEmpty(matchDaysList)) {
                logger.warn("No match days to delete were found by categoryId {} in repository.", categoryId);
                throw new NotFoundException("No match days found in repository by requested categoryId.");
            } else {
                matchDayRepository.deleteAll(matchDaysList);
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing match day by categoryId {} in repository.", categoryId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteMatchDayByCategoryIdAndSeason(String categoryId, Integer season) {
        try {
            List<MatchDay> matchDaysList = matchDayRepository.findByCategoryIdAndSeason(categoryId, season);
            if (CollectionUtils.isEmpty(matchDaysList)) {
                logger.warn("No match days to delete were found by categoryId {} and season {} in repository.",
                        categoryId, season);
                throw new NotFoundException("No match days found in repository by requested categoryId and season.");
            } else {
                matchDayRepository.deleteAll(matchDaysList);
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing match day by categoryId {} and season {} in repository.",
                    categoryId, season, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteMatchDayByCategoryIdSeasonAndNumber(String categoryId, Integer season, Integer number) {
        MatchDayId matchDayId = new MatchDayId(categoryId, season, number);
        try {
            MatchDay matchDay = matchDayRepository.findById(matchDayId)
                    .orElseThrow(() -> new NotFoundException(String.format("Match day with id %s does not exist.", matchDayId)));

            matchDayRepository.delete(matchDay);
        } catch (NotFoundException e) {
            logger.error("Match day with id {} was not found.", matchDayId, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing match day with id {} in repository.", matchDayId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

//    @Async
//    public void storeInKafka(ChangeType changeType, Long matchDayId, MatchDay matchDay) {
//        try {
//            messageProducer.sendMessage(this.KAFKA_TOPIC, new History(
//                    new Date(),
//                    changeType,
//                    "MatchDay",
//                    String.valueOf(matchDayId),
//                    matchDay == null ? null : new ObjectMapper().writeValueAsString(matchDay), // TODO: USE JSON PATCH.
//                    "Admin" // TODO: createMatchDay is not implemented yet.
//            ));
//        } catch (Exception e) {
//            logger.warn("Kafka operation {} with name {} and match day {} need to be reviewed", changeType, matchDayId, matchDay);
//        }
//    }
}

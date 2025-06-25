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
    public List<MatchDayDTO> findAllMatchDays(Long dateFrom, Long dateTo, Boolean synced) {
        List<MatchDay> matchDaysList;
        try {
            if (dateFrom != null && dateTo != null && synced != null) {
                matchDaysList = matchDayRepository.findByStartDateBetweenAndSynced(dateFrom, dateTo, synced);
            } else if (dateFrom != null && dateTo != null) {
                matchDaysList = matchDayRepository.findByStartDateBetween(dateFrom, dateTo);
            } else if (dateFrom != null && synced != null) {
                matchDaysList = matchDayRepository.findByStartDateGreaterThanEqualAndSynced(dateFrom, synced);
            } else if (dateTo != null && synced != null) {
                matchDaysList = matchDayRepository.findByStartDateLessThanEqualAndSynced(dateTo, synced);
            } else if (dateFrom != null) {
                matchDaysList = matchDayRepository.findByStartDateGreaterThanEqual(dateFrom);
            } else if (dateTo != null) {
                matchDaysList = matchDayRepository.findByStartDateLessThanEqual(dateTo);
            } else if (synced != null) {
                matchDaysList = matchDayRepository.findBySynced(synced);
            } else {
                matchDaysList = matchDayRepository.findAll();
            }
        } catch (Exception e) {
            logger.error("Unexpected error while searching match days in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }

        if (CollectionUtils.isEmpty(matchDaysList)) {
            logger.warn("Match days were not found in repository.");
            throw new NotFoundException("Match days not found in repository.");
        }

        return matchDaysList.stream()
                .map(matchDayMapper::matchDayEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MatchDayDTO> findCompletedMatchDays(Long endDateFrom, Long endDateTo, Boolean resultsChecked) {
        List<MatchDay> matchDaysList;
        try {
            if (endDateFrom != null && endDateTo != null && resultsChecked != null) {
                matchDaysList = matchDayRepository.findByEndDateBetweenAndResultsChecked(endDateFrom, endDateTo, resultsChecked);
            } else if (endDateFrom != null && endDateTo != null) {
                matchDaysList = matchDayRepository.findByEndDateBetween(endDateFrom, endDateTo);
            } else if (endDateFrom != null && resultsChecked != null) {
                matchDaysList = matchDayRepository.findByEndDateGreaterThanEqualAndResultsChecked(endDateFrom, resultsChecked);
            } else if (endDateTo != null && resultsChecked != null) {
                matchDaysList = matchDayRepository.findByEndDateLessThanEqualAndResultsChecked(endDateTo, resultsChecked);
            } else if (endDateFrom != null) {
                matchDaysList = matchDayRepository.findByEndDateGreaterThanEqual(endDateFrom);
            } else if (endDateTo != null) {
                matchDaysList = matchDayRepository.findByEndDateLessThanEqual(endDateTo);
            } else if (resultsChecked != null) {
                matchDaysList = matchDayRepository.findByResultsChecked(resultsChecked);
            } else {
                matchDaysList = matchDayRepository.findAll();
            }
        } catch (Exception e) {
            logger.error("Unexpected error while searching completed match days in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }

        if (CollectionUtils.isEmpty(matchDaysList)) {
            logger.warn("Completed match days were not found in repository.");
            throw new NotFoundException("Completed match days not found in repository.");
        }

        return matchDaysList.stream()
                .map(matchDayMapper::matchDayEntityToDto)
                .collect(Collectors.toList());
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

    @Transactional
    public List<MatchDay> createMatchDays(List<MatchDayDTO> matchDayDTOList) {
        try {
            List<MatchDay> matchDays = matchDayDTOList.stream()
                    .map(matchDayMapper::matchDayDtoToEntity)
                    .toList();

            return matchDayRepository.saveAll(matchDays);
        } catch (Exception e) {
            logger.error("Unexpected error while creating several match days in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void updateMatchDay(MatchDayDTO matchDayDTO) {
        MatchDay matchDay = matchDayRepository.findById(
                        new MatchDayId(matchDayDTO.getCategoryId(),
                                matchDayDTO.getSeason(), matchDayDTO.getMatchDayNumber()))
                .orElseThrow(() -> new NotFoundException("Match day not found"));

        matchDay.setSynced(matchDayDTO.isSynced());

        try {
            matchDayRepository.save(matchDay);
        } catch (Exception e) {
            logger.error("Error updating match day", e);
            throw new InternalServerErrorException("Error updating match day");
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
            logger.warn("Match days were not found by categoryId {} in repository.", categoryId);
            throw new NotFoundException("Match days not found in repository by requested categoryId.");
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
            logger.warn("Match days were not found by categoryId {} and season {} in repository.", categoryId, season);
            throw new NotFoundException("Match days not found in repository by requested categoryId and season.");
        } else {
            return matchDaysList.stream()
                    .map(matchDayMapper::matchDayEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public void deleteMatchDaysByCategoryId(String categoryId) {
        try {
            List<MatchDay> matchDaysList = matchDayRepository.findByCategoryId(categoryId);
            if (CollectionUtils.isEmpty(matchDaysList)) {
                logger.warn("Match days to delete were not found by categoryId {} in repository.", categoryId);
                throw new NotFoundException("Match days not found in repository by requested categoryId.");
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
    public void deleteMatchDaysByCategoryIdAndSeason(String categoryId, Integer season) {
        try {
            List<MatchDay> matchDaysList = matchDayRepository.findByCategoryIdAndSeason(categoryId, season);
            if (CollectionUtils.isEmpty(matchDaysList)) {
                logger.warn("Match days to delete were not found by categoryId {} and season {} in repository.",
                        categoryId, season);
                throw new NotFoundException("Match days not found in repository by requested categoryId and season.");
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

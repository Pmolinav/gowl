package com.pmolinav.predictions.services;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.repositories.MatchRepository;
import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.mapper.MatchMapper;
import com.pmolinav.predictionslib.model.Match;
import com.pmolinav.predictionslib.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@EnableAsync
@Service
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public MatchService(MatchRepository matchRepository, MatchMapper matchMapper) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<MatchDTO> findAllMatches() {
        List<Match> matches;
        try {
            matches = matchRepository.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while searching all matches in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(matches)) {
            logger.warn("Matches were not found in repository.");
            throw new NotFoundException("Matches not found in repository.");
        } else {
            return matches.stream()
                    .map(matchMapper::matchEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public Match createMatch(MatchDTO matchDTO) {
        try {
            Match match = matchMapper.matchDtoToEntity(matchDTO);
            return matchRepository.save(match);
        } catch (Exception e) {
            logger.error("Unexpected error while creating new match in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
    
    @Transactional
    public void updateMatch(Long matchId, MatchDTO matchDTO) {
        try {
            Match existing = matchRepository.findById(matchId)
                    .orElseThrow(() -> new NotFoundException("Match with id " + matchId + " not found."));

            Match updated = matchMapper.matchDtoToEntity(matchDTO);
            updated.setMatchId(existing.getMatchId()); // ensure ID is preserved

            matchRepository.save(updated);
        } catch (NotFoundException e) {
            logger.error("Match not found with id: {}", matchId);
            throw e;
        } catch (Exception e) {
            logger.error("Error while updating match with id {}", matchId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public MatchDTO findByMatchId(Long matchId) {
        Optional<Match> optionalMatch;
        try {
            optionalMatch = matchRepository.findById(matchId);
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving match with id {} from repository.", matchId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (optionalMatch.isPresent()) {
            return matchMapper.matchEntityToDto(optionalMatch.get());
        } else {
            logger.warn("Match with id {} was not found.", matchId);
            throw new NotFoundException("Match not found for id: " + matchId);
        }
    }

    @Transactional(readOnly = true)
    public List<MatchDTO> findByCategoryIdAndSeasonAndMatchDayNumber(String categoryId, Integer season, Integer matchDayNumber) {
        List<Match> matches;
        try {
            matches = matchRepository.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, matchDayNumber);
        } catch (Exception e) {
            logger.error("Unexpected error while searching matches by categoryId: {}, season: {} and matchDayNumber: {} in repository.",
                    categoryId, season, matchDayNumber, e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(matches)) {
            logger.warn("Matches not found for categoryId: {}, season: {} and matchDayNumber: {} in repository.",
                    categoryId, season, matchDayNumber);
            throw new NotFoundException("Matches not found for requested criteria.");
        } else {
            return matches.stream()
                    .map(matchMapper::matchEntityToDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public void deleteByMatchId(Long matchId) {
        try {
            if (!matchRepository.existsById(matchId)) {
                logger.warn("Match with id {} was not found in repository.", matchId);
                throw new NotFoundException("Match not found with id: " + matchId);
            }
            matchRepository.deleteById(matchId);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while deleting match with id {} from repository.", matchId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteByCategoryIdAndSeasonAndMatchDayNumber(String categoryId, Integer season, Integer matchDayNumber) {
        List<Match> matches;
        try {
            matches = matchRepository.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, matchDayNumber);
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving matches for deletion for categoryId: {}, season: {} and matchDayNumber: {}.",
                    categoryId, season, matchDayNumber, e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(matches)) {
            logger.warn("Matches for deletion not found for categoryId: {}, season: {} and matchDayNumber: {}.",
                    categoryId, season, matchDayNumber);
            throw new NotFoundException("Matches not found for requested criteria.");
        } else {
            try {
                matchRepository.deleteAll(matches);
            } catch (Exception e) {
                logger.error("Unexpected error while deleting matches for categoryId: {}, season: {} and matchDayNumber: {}.",
                        categoryId, season, matchDayNumber, e);
                throw new InternalServerErrorException(e.getMessage());
            }
        }
    }

//    @Async
//    public void storeInKafka(ChangeType changeType, Long matchId, Match match) {
//        try {
//            messageProducer.sendMessage(this.KAFKA_TOPIC, new History(
//                    new Date(),
//                    changeType,
//                    "Match",
//                    String.valueOf(matchId),
//                    match == null ? null : new ObjectMapper().writeValueAsString(match), // TODO: USE JSON PATCH.
//                    "Admin" // TODO: createMatch is not implemented yet.
//            ));
//        } catch (Exception e) {
//            logger.warn("Kafka operation {} with name {} and match day {} need to be reviewed", changeType, matchId, match);
//        }
//    }

}

package com.pmolinav.matchdatasync.services;

import com.pmolinav.matchdatasync.exceptions.InternalServerErrorException;
import com.pmolinav.matchdatasync.repositories.MatchRepository;
import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.mapper.MatchMapper;
import com.pmolinav.predictionslib.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@EnableAsync
@Service
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;

    @Autowired
    public MatchService(MatchRepository matchRepository, MatchMapper matchMapper) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
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
    public Match updateMatch(Match match) {
        try {
            return matchRepository.save(match);
        } catch (Exception e) {
            logger.error("Error updating match entity {}", match.getMatchId(), e);
            throw new InternalServerErrorException("Failed to update match.");
        }
    }

    @Transactional(readOnly = true)
    public List<Match> findByCategoryIdAndSeasonAndMatchDayNumber(String categoryId, Integer season, Integer matchDayNumber) {
        try {
            return matchRepository.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, matchDayNumber);
        } catch (Exception e) {
            logger.error("Unexpected error while searching matches by categoryId: {}, season: {} and matchDayNumber: {} in repository.",
                    categoryId, season, matchDayNumber, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

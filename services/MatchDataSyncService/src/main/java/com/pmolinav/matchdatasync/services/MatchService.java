package com.pmolinav.matchdatasync.services;

import com.pmolinav.matchdatasync.exceptions.InternalServerErrorException;
import com.pmolinav.matchdatasync.exceptions.NotFoundException;
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

}

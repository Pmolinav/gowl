package com.pmolinav.leagues.services;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagues.repositories.LeagueRepository;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.mapper.LeagueMapper;
import com.pmolinav.leagueslib.model.League;
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
public class LeagueService {

    private static final Logger logger = LoggerFactory.getLogger(LeagueService.class);

    private final LeagueRepository leagueRepository;
    private final LeagueMapper leagueMapper;
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public LeagueService(LeagueRepository leagueRepository, LeagueMapper leagueMapper) {
        this.leagueRepository = leagueRepository;
        this.leagueMapper = leagueMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<LeagueDTO> findAllLeagues() {
        List<League> leaguesList;
        try {
            leaguesList = leagueRepository.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while searching all leagues in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(leaguesList)) {
            logger.warn("Leagues were not found in repository.");
            throw new NotFoundException("Leagues not found in repository.");
        } else {
            return leaguesList.stream()
                    .map(leagueMapper::leagueEntityToDto)
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    public LeagueDTO findById(Long id) {
        try {
            League league = leagueRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("League with id %s does not exist.", id)));

            return leagueMapper.leagueEntityToDto(league);
        } catch (NotFoundException e) {
            logger.error("League with id {} was not found.", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while searching league with id {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public LeagueDTO findByName(String name) {
        try {
            League league = leagueRepository.findByName(name)
                    .orElseThrow(() -> new NotFoundException(String.format("League with name %s does not exist.", name)));

            return leagueMapper.leagueEntityToDto(league);
        } catch (NotFoundException e) {
            logger.error("League with name {} was not found.", name, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while searching league with name {} in repository.", name, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public League createLeague(LeagueDTO leagueDTO) {
        try {
            League league = leagueMapper.leagueDtoToEntity(leagueDTO);
            return leagueRepository.save(league);
        } catch (Exception e) {
            logger.error("Unexpected error while creating new league in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void closeLeagueById(Long id) {
        try {
            int result = leagueRepository.closeLeagueById(id);
            logger.debug("Closed league with id {} successfully with result {}", id, result);
        } catch (Exception e) {
            logger.error("Unexpected error while closing league ID {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void closeLeagueByName(String name) {
        try {
            int result = leagueRepository.closeLeagueByName(name);
            logger.debug("Closed league with name {} successfully with result {}", name, result);
        } catch (Exception e) {
            logger.error("Unexpected error while closing league name {} in repository.", name, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteLeague(Long id) {
        try {
            League league = leagueRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("League with id %s does not exist.", id)));

            leagueRepository.delete(league);
        } catch (NotFoundException e) {
            logger.error("League with id {} was not found.", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing league with id {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteLeagueByName(String name) {
        try {
            League league = leagueRepository.findByName(name)
                    .orElseThrow(() -> new NotFoundException(String.format("League with name %s does not exist.", name)));

            leagueRepository.delete(league);
        } catch (NotFoundException e) {
            logger.error("League with name {} was not found.", name, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing league with name {} in repository.", name, e);
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

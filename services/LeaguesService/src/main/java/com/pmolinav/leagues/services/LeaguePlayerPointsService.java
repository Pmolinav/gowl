package com.pmolinav.leagues.services;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.repositories.LeaguePlayerPointsRepository;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import com.pmolinav.leagueslib.mapper.LeaguePlayerPointsMapper;
import com.pmolinav.leagueslib.model.LeaguePlayerPoints;
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
public class LeaguePlayerPointsService {

    private static final Logger logger = LoggerFactory.getLogger(LeaguePlayerPointsService.class);

    private final LeaguePlayerPointsRepository leaguePlayerPointsRepository;
    private final LeaguePlayerPointsMapper leaguePlayerPointsMapper;
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public LeaguePlayerPointsService(LeaguePlayerPointsRepository leaguePlayerPointsRepository,
                                     LeaguePlayerPointsMapper leaguePlayerPointsMapper) {
        this.leaguePlayerPointsRepository = leaguePlayerPointsRepository;
        this.leaguePlayerPointsMapper = leaguePlayerPointsMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<LeaguePlayerPointsDTO> findByLeagueIdAndPlayer(Long id, String username) {
        List<LeaguePlayerPoints> leaguePlayerPoints;
        try {
            leaguePlayerPoints = leaguePlayerPointsRepository.findByLeagueIdAndUsername(id, username);
        } catch (Exception e) {
            logger.error("Unexpected error while searching league player points by leagueId {} and username {} in repository.",
                    id, username, e);
            throw new InternalServerErrorException(e.getMessage());
        }

        if (CollectionUtils.isEmpty(leaguePlayerPoints)) {
            logger.warn("No league player points were found in repository by leagueId {} and username {}.", id, username);
            throw new NotFoundException("No league player points found in repository by requested leagueId and username.");
        }

        return leaguePlayerPoints.stream()
                .map(leaguePlayerPointsMapper::leaguePlayerPointsEntityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LeaguePlayerPointsDTO> findByCategoryIdSeasonAndNumber(String categoryId, Integer season, Integer number) {
        List<LeaguePlayerPoints> leaguePlayerPoints;
        try {
            leaguePlayerPoints = leaguePlayerPointsRepository.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, number);
        } catch (Exception e) {
            logger.error("Unexpected error while searching league player points by categoryId {}, season {} and number {} in repository.",
                    categoryId, season, number, e);
            throw new InternalServerErrorException(e.getMessage());
        }

        if (CollectionUtils.isEmpty(leaguePlayerPoints)) {
            logger.warn("No league player points were found in repository by categoryId {}, season {} and number {}.",
                    categoryId, season, number);
            throw new NotFoundException("No league player points found in repository by requested categoryId, season and number.");
        }

        return leaguePlayerPoints.stream()
                .map(leaguePlayerPointsMapper::leaguePlayerPointsEntityToDto)
                .toList();
    }

    @Transactional
    public LeaguePlayerPointsDTO createLeaguePlayerPoints(LeaguePlayerPointsDTO leaguePlayerPoints) {
        try {
            LeaguePlayerPoints entity = leaguePlayerPointsMapper.leaguePlayerPointsDtoToEntity(leaguePlayerPoints);
            return leaguePlayerPointsMapper.leaguePlayerPointsEntityToDto(leaguePlayerPointsRepository.save(entity));
        } catch (Exception e) {
            logger.error("Unexpected error while creating new league player points {} in repository.", leaguePlayerPoints, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteLeaguePlayersByLeagueIdAndUsername(Long id, String username) {
        try {
            List<LeaguePlayerPoints> leaguePlayerPointsList = leaguePlayerPointsRepository.findByLeagueIdAndUsername(id, username);

            if (CollectionUtils.isEmpty(leaguePlayerPointsList)) {
                logger.warn("No league player points to delete were found by leagueId {} and username {} in repository.",
                        id, username);
                throw new NotFoundException("No league player points found in repository by requested leagueId and username.");
            } else {
                leaguePlayerPointsRepository.deleteAll(leaguePlayerPointsList);
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing league player points by leagueId {} and username {} in repository.", id, username, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteLeaguePlayersByCategoryIdSeasonAndNumber(String categoryId, Integer season, Integer number) {
        try {
            List<LeaguePlayerPoints> leaguePlayerPointsList =
                    leaguePlayerPointsRepository.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, number);

            if (CollectionUtils.isEmpty(leaguePlayerPointsList)) {
                logger.warn("No league player points to delete were found by categoryId {}, season {} and number {} in repository.",
                        categoryId, season, number);
                throw new NotFoundException("No league player points found in repository by requested categoryId, season and number.");
            } else {
                leaguePlayerPointsRepository.deleteAll(leaguePlayerPointsList);
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing league player points by categoryId {}, season {} and number {} in repository.",
                    categoryId, season, number, e);
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

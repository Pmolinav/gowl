package com.pmolinav.leagues.services;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.repositories.LeaguePlayerPointsRepository;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import com.pmolinav.leagueslib.mapper.LeaguePlayerPointsMapper;
import com.pmolinav.leagueslib.model.LeaguePlayerPoints;
import com.pmolinav.leagueslib.model.LeaguePlayerPointsId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

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
            logger.warn("League player points were not found in repository by leagueId {} and username {}.", id, username);
            throw new NotFoundException("League player points not found in repository by requested leagueId and username.");
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
            logger.warn("League player points were not found in repository by categoryId {}, season {} and number {}.",
                    categoryId, season, number);
            throw new NotFoundException("League player points not found in repository by requested categoryId, season and number.");
        }

        return leaguePlayerPoints.stream()
                .map(leaguePlayerPointsMapper::leaguePlayerPointsEntityToDto)
                .toList();
    }

    @Transactional
    public LeaguePlayerPointsDTO createOrUpdateLeaguePlayerPoints(LeaguePlayerPointsDTO leaguePlayerPoints) {
        try {
            LeaguePlayerPoints entity;
            Optional<LeaguePlayerPoints> optionalEntity = leaguePlayerPointsRepository.findById(new LeaguePlayerPointsId(
                    leaguePlayerPoints.getCategoryId(),
                    leaguePlayerPoints.getSeason(),
                    leaguePlayerPoints.getMatchDayNumber(),
                    leaguePlayerPoints.getLeagueId(),
                    leaguePlayerPoints.getUsername()
            ));
            // If present, points are updated.
            if (optionalEntity.isPresent()) {
                entity = optionalEntity.get();
                entity.setPoints(entity.getPoints() + leaguePlayerPoints.getPoints());
                entity = leaguePlayerPointsRepository.save(entity);
            } else {
                entity = leaguePlayerPointsRepository.save(leaguePlayerPointsMapper.leaguePlayerPointsDtoToEntity(leaguePlayerPoints));
            }
            return leaguePlayerPointsMapper.leaguePlayerPointsEntityToDto(entity);
        } catch (Exception e) {
            logger.error("Unexpected error while creating or updating league player points {} in repository.", leaguePlayerPoints, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteLeaguePlayerPointsByLeagueIdAndUsername(Long id, String username) {
        try {
            List<LeaguePlayerPoints> leaguePlayerPointsList = leaguePlayerPointsRepository.findByLeagueIdAndUsername(id, username);

            if (CollectionUtils.isEmpty(leaguePlayerPointsList)) {
                logger.warn("League player points to delete were not found by leagueId {} and username {} in repository.",
                        id, username);
                throw new NotFoundException("League player points not found in repository by requested leagueId and username.");
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
    public void deleteLeaguePlayerPointsByCategoryIdSeasonAndNumber(String categoryId, Integer season, Integer number) {
        try {
            List<LeaguePlayerPoints> leaguePlayerPointsList =
                    leaguePlayerPointsRepository.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, number);

            if (CollectionUtils.isEmpty(leaguePlayerPointsList)) {
                logger.warn("League player points to delete were not found by categoryId {}, season {} and number {} in repository.",
                        categoryId, season, number);
                throw new NotFoundException("League player points not found in repository by requested categoryId, season and number.");
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

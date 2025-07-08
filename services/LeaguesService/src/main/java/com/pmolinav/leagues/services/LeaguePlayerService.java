package com.pmolinav.leagues.services;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagues.repositories.LeaguePlayerRepository;
import com.pmolinav.leagues.repositories.LeagueRepository;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.mapper.LeagueMapper;
import com.pmolinav.leagueslib.mapper.LeaguePlayerMapper;
import com.pmolinav.leagueslib.model.League;
import com.pmolinav.leagueslib.model.LeaguePlayer;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
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
public class LeaguePlayerService {

    private static final Logger logger = LoggerFactory.getLogger(LeaguePlayerService.class);

    private final LeaguePlayerRepository leaguePlayerRepository;
    private final LeagueRepository leagueRepository;
    private final LeaguePlayerMapper leaguePlayerMapper;
    private final LeagueMapper leagueMapper;
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public LeaguePlayerService(LeaguePlayerRepository leaguePlayerRepository,
                               LeagueRepository leagueRepository,
                               LeaguePlayerMapper leaguePlayerMapper,
                               LeagueMapper leagueMapper) {
        this.leaguePlayerRepository = leaguePlayerRepository;
        this.leagueRepository = leagueRepository;
        this.leaguePlayerMapper = leaguePlayerMapper;
        this.leagueMapper = leagueMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<LeaguePlayerDTO> findLeaguePlayersByLeagueId(Long id) {
        List<LeaguePlayer> leaguePlayers;
        try {
            leaguePlayers = leaguePlayerRepository.findByLeagueId(id);
        } catch (Exception e) {
            logger.error("Unexpected error while searching league players by leagueId {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(leaguePlayers)) {
            logger.warn("League players were not found in repository by leagueId {}.", id);
            throw new NotFoundException("League layers not found in repository by requested leagueId.");
        } else {
            return leaguePlayers.stream()
                    .map(leaguePlayerMapper::leaguePlayerEntityToDto)
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    public LeaguePlayerDTO findLeaguePlayerByLeagueIdAndPlayer(Long id, String username) {
        try {
            LeaguePlayer leaguePlayer = leaguePlayerRepository.findById(new LeaguePlayerId(id, username))
                    .orElseThrow(() -> new NotFoundException(
                            String.format("League player with id %s and username %s does not exist.", id, username)));

            return leaguePlayerMapper.leaguePlayerEntityToDto(leaguePlayer);
        } catch (NotFoundException e) {
            logger.error("League player with leagueId {} and username {} was not found.", id, username, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while searching league player with leagueId {} and username {} in repository.", id, username, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<LeagueDTO> findLeaguesByUsername(String username) {
        List<LeaguePlayer> leaguePlayers;
        try {
            leaguePlayers = leaguePlayerRepository.findByUsername(username);
        } catch (Exception e) {
            logger.error("Unexpected error while searching league players by username {} in repository.", username, e);
            throw new InternalServerErrorException(e.getMessage());
        }

        if (CollectionUtils.isEmpty(leaguePlayers)) {
            logger.warn("League players were not found in repository by username {}.", username);
            throw new NotFoundException("League players not found in repository by requested username.");
        }

        List<Long> leagueIds = leaguePlayers.stream()
                .map(LeaguePlayer::getLeagueId)
                .toList();

        List<League> leagues;
        try {
            leagues = leagueRepository.findByLeagueIdIn(leagueIds);
        } catch (Exception e) {
            logger.error("Unexpected error while searching leagues by ids {}.", leagueIds, e);
            throw new InternalServerErrorException(e.getMessage());
        }

        if (CollectionUtils.isEmpty(leagues)) {
            logger.warn("Leagues were not found in repository for the given league ids {}.", leagueIds);
            throw new NotFoundException("Leagues not found for the requested user.");
        }

        return leagues.stream()
                .map(leagueMapper::leagueEntityToDto)
                .toList();
    }

    @Transactional
    public List<LeaguePlayerId> createLeaguePlayers(List<LeaguePlayerDTO> leaguePlayers) {
        try {
            List<LeaguePlayer> players = leaguePlayers.stream()
                    .map(leaguePlayerMapper::leaguePlayerDtoToEntity)
                    .toList();

            return leaguePlayerRepository.saveAll(players).stream()
                    .map(leaguePlayer -> new LeaguePlayerId(leaguePlayer.getLeagueId(), leaguePlayer.getUsername()))
                    .toList();
        } catch (Exception e) {
            logger.error("Unexpected error while creating new league players {} in repository.", leaguePlayers, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void addPointsToLeaguePlayer(Long leagueId, String username, Integer points) {
        try {
            int updated = leaguePlayerRepository.addPointsToLeaguePlayer(leagueId, username, points);
            if (updated == 0) {
                throw new NotFoundException(
                        String.format("League player with id %s and username %s does not exist.", leagueId, username));
            }
        } catch (NotFoundException e) {
            logger.error("League player with leagueId {} and username {} was not found.", leagueId, username, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while updating points for league player with leagueId {} and username {}.", leagueId, username, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteLeaguePlayersByLeagueId(Long id) {
        try {
            List<LeaguePlayer> leaguePlayers = leaguePlayerRepository.findByLeagueId(id);
            if (CollectionUtils.isEmpty(leaguePlayers)) {
                logger.warn("League players to delete were not found by leagueId {} in repository.", id);
                throw new NotFoundException("League players not found in repository by requested leagueId.");
            } else {
                leaguePlayerRepository.deleteAll(leaguePlayers);
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing league players by leagueId {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteLeaguePlayersByLeagueIdAndUsername(Long id, String username) {
        try {
            LeaguePlayer leaguePlayer = leaguePlayerRepository.findById(new LeaguePlayerId(id, username))
                    .orElseThrow(() -> new NotFoundException(
                            String.format("League player with id %s and username %s does not exist.", id, username)));

            leaguePlayerRepository.delete(leaguePlayer);
        } catch (NotFoundException e) {
            logger.error("League player with id {} and username {} was not found.", id, username, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing league players by leagueId {} and username {} in repository.", id, username, e);
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

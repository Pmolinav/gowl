package com.pmolinav.leagues.services;

import com.pmolinav.leagues.repositories.LeagueCategoryRepository;
import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.mapper.LeagueCategoryMapper;
import com.pmolinav.leagueslib.model.LeagueCategory;
import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
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
public class LeagueCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(LeagueCategoryService.class);

    private final LeagueCategoryRepository leagueCategoryRepository;
    private final LeagueCategoryMapper leagueCategoryMapper;
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public LeagueCategoryService(LeagueCategoryRepository leagueCategoryRepository, LeagueCategoryMapper leagueCategoryMapper) {
        this.leagueCategoryRepository = leagueCategoryRepository;
        this.leagueCategoryMapper = leagueCategoryMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<LeagueCategory> findAllLeagueCategories() {
        List<LeagueCategory> leagueCategoriesList;
        try {
            leagueCategoriesList = leagueCategoryRepository.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while searching all league categories in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(leagueCategoriesList)) {
            logger.warn("League categories were not found in repository.");
            throw new NotFoundException("League categories not found in repository.");
        } else {
            return leagueCategoriesList;
        }
    }

    @Transactional
    public LeagueCategory createLeagueCategory(LeagueCategoryDTO leagueCategoryDTO) {
        try {
            LeagueCategory leagueCategory = leagueCategoryMapper.leagueCategoryDtoToEntity(leagueCategoryDTO);
            return leagueCategoryRepository.save(leagueCategory);
        } catch (Exception e) {
            logger.error("Unexpected error while creating new league category in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public LeagueCategory findById(String id) {
        try {
            return leagueCategoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("League category with id %s does not exist.", id)));
        } catch (NotFoundException e) {
            logger.error("League category with id {} was not found.", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while searching league category with id {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteLeagueCategory(String id) {
        try {
            LeagueCategory leagueCategory = leagueCategoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("League category with id %s does not exist.", id)));

            leagueCategoryRepository.delete(leagueCategory);
        } catch (NotFoundException e) {
            logger.error("League category with id {} was not found.", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing league category with id {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

//    @Async
//    public void storeInKafka(ChangeType changeType, Long leagueCategoryId, LeagueCategory leagueCategory) {
//        try {
//            messageProducer.sendMessage(this.KAFKA_TOPIC, new History(
//                    new Date(),
//                    changeType,
//                    "LeagueCategory",
//                    String.valueOf(leagueCategoryId),
//                    leagueCategory == null ? null : new ObjectMapper().writeValueAsString(leagueCategory), // TODO: USE JSON PATCH.
//                    "Admin" // TODO: createLeagueCategory is not implemented yet.
//            ));
//        } catch (Exception e) {
//            logger.warn("Kafka operation {} with name {} and league category {} need to be reviewed", changeType, leagueCategoryId, leagueCategory);
//        }
//    }
}

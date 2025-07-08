package com.pmolinav.leagues.services;

import com.pmolinav.leagues.clients.LeagueCategoriesClient;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.model.LeagueCategory;
import feign.FeignException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueCategoriesBOService {

    private static final Logger logger = LoggerFactory.getLogger(LeagueCategoriesBOService.class);

    @Autowired
    private LeagueCategoriesClient leagueCategoriesClient;

    public List<LeagueCategory> findAllLeagueCategories() {
        try {
            return leagueCategoriesClient.findAllLeagueCategories();
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League categories not found.", e);
                throw new NotFoundException("League categories not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public LeagueCategory findLeagueCategoryById(String id) {
        try {
            return leagueCategoriesClient.findLeagueCategoryById(id);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League category with id {} not found.", id, e);
                throw new NotFoundException("League category " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public String createLeagueCategory(LeagueCategoryDTO leagueCategoryDTO) {
        try {
            return leagueCategoriesClient.createLeagueCategory(leagueCategoryDTO);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteLeagueCategory(String id) {
        try {
            leagueCategoriesClient.deleteLeagueCategory(id);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("League category with id {} not found.", id, e);
                throw new NotFoundException("League category " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}

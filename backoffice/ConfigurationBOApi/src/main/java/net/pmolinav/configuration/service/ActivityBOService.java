package net.pmolinav.configuration.service;

import feign.FeignException;
import feign.RetryableException;
import net.pmolinav.bookingslib.dto.ActivityDTO;
import net.pmolinav.bookingslib.exception.CustomStatusException;
import net.pmolinav.bookingslib.exception.InternalServerErrorException;
import net.pmolinav.bookingslib.exception.NotFoundException;
import net.pmolinav.bookingslib.model.Activity;
import net.pmolinav.configuration.client.ActivityClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityBOService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityBOService.class);

    @Autowired
    private ActivityClient activityClient;

    public List<Activity> findAllActivities() {
        try {
            return activityClient.findAllActivities();
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("No activities found.", e);
                throw new NotFoundException("No activities found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public String createActivity(ActivityDTO activityDTO) {
        try {
            return activityClient.createActivity(activityDTO);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Activity findActivityByName(String name) {
        try {
            return activityClient.findActivityByName(name);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Activity with name {} not found.", name, e);
                throw new NotFoundException("Activity " + name + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteActivity(String name) {
        try {
            activityClient.deleteActivity(name);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected retryable error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Activity with id {} not found.", name, e);
                throw new NotFoundException("Activity " + name + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

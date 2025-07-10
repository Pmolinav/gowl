package com.pmolinav.predictions.services;

import com.pmolinav.predictions.clients.PredictionsServiceClient;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.EventDTO;
import feign.FeignException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventBOService {

    private static final Logger logger = LoggerFactory.getLogger(EventBOService.class);

    @Autowired
    private PredictionsServiceClient predictionsServiceClient;

    public List<EventDTO> findAllEvents() {
        try {
            return predictionsServiceClient.findAll();
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Events not found.", e);
                throw new NotFoundException("Events not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public EventDTO findEventByType(String type) {
        try {
            return predictionsServiceClient.findByType(type);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Event with type {} not found.", type, e);
                throw new NotFoundException("Event " + type + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Long createEvent(EventDTO eventDTO) {
        try {
            return predictionsServiceClient.create(eventDTO);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling create.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void updateEvent(String type, EventDTO eventDTO) {
        try {
            predictionsServiceClient.update(type, eventDTO);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling update with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Event with type {} not found during update.", type, e);
                throw new NotFoundException("Event " + type + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling update.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteEvent(String type) {
        try {
            predictionsServiceClient.delete(type);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Event with type {} not found.", type, e);
                throw new NotFoundException("Event " + type + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
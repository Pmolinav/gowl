package com.pmolinav.predictions.services;

import com.pmolinav.predictions.clients.EventClient;
import com.pmolinav.predictions.exceptions.CustomStatusException;
import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
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
    private EventClient eventClient;

    public List<EventDTO> findAllEvents() {
        try {
            return eventClient.findAll();
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

    public EventDTO findEventById(Long id) {
        try {
            return eventClient.findById(id);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Event with id {} not found.", id, e);
                throw new NotFoundException("Event " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Long createEvent(EventDTO eventDTO) {
        try {
            return eventClient.create(eventDTO);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling create.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void updateEvent(Long id, EventDTO eventDTO) {
        try {
            eventClient.update(id, eventDTO);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling update with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Event with id {} not found during update.", id, e);
                throw new NotFoundException("Event " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling update.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteEvent(Long id) {
        try {
            eventClient.delete(id);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Event with id {} not found.", id, e);
                throw new NotFoundException("Event " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
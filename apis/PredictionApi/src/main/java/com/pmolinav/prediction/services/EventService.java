package com.pmolinav.prediction.services;

import com.pmolinav.prediction.clients.PredictionsServiceClient;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private PredictionsServiceClient predictionsServiceClient;

    public EventDTO findEventByEventType(String type) {
        try {
            return predictionsServiceClient.findByEventType(type);
        } catch (FeignException e) {
            if (e.status() != 404) {
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
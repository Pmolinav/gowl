
package com.pmolinav.leagues.services;

import com.pmolinav.leagues.clients.HealthClient;
import com.pmolinav.leagues.exceptions.CustomStatusException;
import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import feign.FeignException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthBOService {

    private static final Logger logger = LoggerFactory.getLogger(HealthBOService.class);

    @Autowired
    private HealthClient healthClient;

    public void health() {
        try {
            healthClient.health();
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Leagues health not found.", e);
                throw new NotFoundException("Leagues health not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}

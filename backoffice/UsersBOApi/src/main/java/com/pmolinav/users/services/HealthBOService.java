
package com.pmolinav.users.services;

import com.pmolinav.users.clients.HealthClient;
import com.pmolinav.users.exceptions.CustomStatusException;
import com.pmolinav.users.exceptions.InternalServerErrorException;
import com.pmolinav.users.exceptions.NotFoundException;
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
                logger.warn("Users health not found.", e);
                throw new NotFoundException("Users health not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}

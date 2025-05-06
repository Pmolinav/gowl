
package com.pmolinav.auth.services;

import com.pmolinav.auth.clients.HealthClient;
import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.exceptions.InternalServerErrorException;
import com.pmolinav.auth.exceptions.NotFoundException;
import feign.FeignException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthHealthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthHealthService.class);

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
                logger.warn("No activities found.", e);
                throw new NotFoundException("No activities found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}

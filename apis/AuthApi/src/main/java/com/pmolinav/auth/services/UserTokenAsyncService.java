package com.pmolinav.auth.services;

import com.pmolinav.auth.clients.UserTokenClient;
import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.exceptions.InternalServerErrorException;
import com.pmolinav.userslib.dto.LogoutDTO;
import com.pmolinav.userslib.dto.UserTokenDTO;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserTokenAsyncService {

    private static final Logger logger = LoggerFactory.getLogger(UserTokenAsyncService.class);

    private final UserTokenClient userTokenClient;

    @Autowired
    public UserTokenAsyncService(UserTokenClient userTokenClient) {
        this.userTokenClient = userTokenClient;
    }

    @Async
    public void saveUserTokenAsync(String username, String oldRefreshToken, String refreshToken,
                                   String deviceInfo, String ipAddress, LocalDateTime expiresAt) {
        try {
            // If an old refresh token is present, it is invalidated previously.
            if (oldRefreshToken != null) {
                invalidateToken(new LogoutDTO(username, oldRefreshToken));
            }
            userTokenClient.saveUserToken(new UserTokenDTO(username, refreshToken, deviceInfo, ipAddress, expiresAt));
            logger.info("Asynchronous call: User token saved for user {}", username);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public boolean existsTokenForUser(String username, String refreshToken) {
        try {
            return userTokenClient.existsTokenForUser(username, refreshToken);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void invalidateToken(LogoutDTO logoutDTO) {
        try {
            userTokenClient.invalidateToken(logoutDTO);
            logger.info("Token invalidated: {}", logoutDTO);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void invalidateAllTokens(String username) {
        try {
            userTokenClient.invalidateAllTokens(username);
            logger.info("Tokens invalidated for user {}", username);
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

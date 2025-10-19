package com.pmolinav.auth.services;

import com.pmolinav.auth.clients.UserTokenClient;
import com.pmolinav.userslib.dto.UserTokenDTO;
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
    public void saveUserTokenAsync(String username, String refreshToken, String deviceInfo, String ipAddress, LocalDateTime expiresAt) {
        try {
            userTokenClient.saveUserToken(new UserTokenDTO(username, refreshToken, deviceInfo, ipAddress, expiresAt));
            logger.info("Asynchronous call: User token saved for user {}", username);
        } catch (Exception e) {
            logger.error("Failed to save user token asynchronously for user {}: {}", username, e.getMessage());
        }
    }
}

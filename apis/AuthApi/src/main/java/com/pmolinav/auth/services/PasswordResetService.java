package com.pmolinav.auth.services;

import com.pmolinav.auth.clients.UserClient;
import com.pmolinav.auth.exceptions.CustomStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    private final UserClient userClient;
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;


    @Value("${reset.password.code.expiration-minutes}")
    private long expirationMinutes;

    @Value("${reset.password.rate-limit.max-attempts}")
    private int maxAttemptsPerHour;

    @Value("${reset.password.rate-limit.window-minutes}")
    private long attemptWindowMinutes;

    @Value("${reset.password.token.expiration-minutes}")
    private long tokenExpirationMinutes;

    private static final String CODE_PREFIX = "verification-code:";
    private static final String TOKEN_PREFIX = "reset-token:";
    private static final String ATTEMPTS_PREFIX = "email-attempts:";
    private static final String CODE_ATTEMPTS_PREFIX = "code-attempts:";

    @Autowired
    public PasswordResetService(UserClient userClient, StringRedisTemplate redisTemplate, JavaMailSender mailSender) {
        this.userClient = userClient;
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
    }

    public void sendResetCode(String email, String ipAddress) {
        if (isRateLimited(ATTEMPTS_PREFIX, email) || isRateLimited(ATTEMPTS_PREFIX, ipAddress)) {
            throw new CustomStatusException("Too many requests. Try again later.", HttpStatus.TOO_MANY_REQUESTS);
        }

        Boolean exists = userClient.existsByEmail(email);
        if (exists == null || !exists) {
            return;
        }

        String code = String.format("%04d", new Random().nextInt(10000));
        String key = CODE_PREFIX + email;

        redisTemplate.opsForValue().set(key, code, expirationMinutes, TimeUnit.MINUTES);
        sendEmail(email, code);
    }

    public String validateCode(String email, String code, String ipAddress) {
        if (isRateLimited(CODE_ATTEMPTS_PREFIX, email) || isRateLimited(CODE_ATTEMPTS_PREFIX, ipAddress)) {
            throw new CustomStatusException("Too many requests. Try again later.", HttpStatus.TOO_MANY_REQUESTS);
        }
        String key = CODE_PREFIX + email;
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(key);
            // Remove user attempts keys.
            redisTemplate.delete(ATTEMPTS_PREFIX + email);
            redisTemplate.delete(ATTEMPTS_PREFIX + ipAddress);
            redisTemplate.delete(CODE_ATTEMPTS_PREFIX + email);
            redisTemplate.delete(CODE_ATTEMPTS_PREFIX + ipAddress);

            String token = UUID.randomUUID().toString();
            String tokenKey = TOKEN_PREFIX + email;

            // Token to allow user to update password.
            redisTemplate.opsForValue().set(tokenKey, token, tokenExpirationMinutes, TimeUnit.MINUTES);

            logger.info("Verification code validated. Reset token {} generated for {}", token, email);
            return token;
        }

        logger.warn("Verification code {} does not exists for email {}", code, email);
        return null;
    }

    public void updatePassword(String email, String newPassword, String token) {
        String tokenKey = TOKEN_PREFIX + email;
        String storedToken = redisTemplate.opsForValue().get(tokenKey);

        if (storedToken == null || !storedToken.equals(token)) {
            throw new CustomStatusException("Invalid or expired reset token.", HttpStatus.UNAUTHORIZED);
        }
        redisTemplate.delete(tokenKey);

        userClient.updateUserPasswordByEmail(email, newPassword);

        logger.info("Password updated successfully for email {}", email);
    }

    private void sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification code to restore your password");
        message.setText("This is your one-time verification code: " + code +
                "\nThis code will expire after ten minutes.");
        mailSender.send(message);
        logger.info("Verification code was sent properly for email {}", email);
    }

    private boolean isRateLimited(String prefix, String key) {
        if (key != null) {
            String redisKey = prefix + key;
            Long attempts = redisTemplate.opsForValue().increment(redisKey);

            if (attempts > maxAttemptsPerHour) {
                logger.warn("Rate limit reached for {}", key);
                return true;
            } else if (attempts == 1) {
                redisTemplate.expire(redisKey, attemptWindowMinutes, TimeUnit.MINUTES);
            }
        }
        return false;
    }
}

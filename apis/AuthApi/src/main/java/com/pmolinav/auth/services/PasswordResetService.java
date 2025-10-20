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

    private static final String PREFIX = "verification-code:";
    private static final String ATTEMPT_PREFIX = "password-reset-attempts:";

    @Autowired
    public PasswordResetService(UserClient userClient, StringRedisTemplate redisTemplate, JavaMailSender mailSender) {
        this.userClient = userClient;
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
    }

    public void sendResetCode(String email, String ipAddress) {
        if (isRateLimited(email) || isRateLimited(ipAddress)) {
            throw new CustomStatusException("Too many requests. Try again later.", HttpStatus.TOO_MANY_REQUESTS);
        }

        Boolean exists = userClient.existsByEmail(email);
        if (exists == null || !exists) {
            return;
        }

        String code = String.format("%04d", new Random().nextInt(10000));
        String key = PREFIX + email;

        redisTemplate.opsForValue().set(key, code, expirationMinutes, TimeUnit.MINUTES);
        sendEmail(email, code);
    }

    public boolean validateCode(String email, String code) {
        String key = PREFIX + email;
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(key);
            redisTemplate.delete(ATTEMPT_PREFIX + email);
            return true;
        }
        return false;
    }

    public void updatePassword(String email, String newPassword) {
        userClient.updateUserPasswordByEmail(email, newPassword);
    }

    private void sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verification code to restore your password");
        message.setText("This is your one-time verification code: " + code +
                "\nThis code will expire after ten minutes.");
        mailSender.send(message);
        logger.debug("Verification code was sent properly for email {}", email);
    }

    private boolean isRateLimited(String key) {
        String redisKey = ATTEMPT_PREFIX + key;
        Long attempts = redisTemplate.opsForValue().increment(redisKey);

        if (attempts == 1) {
            redisTemplate.expire(redisKey, attemptWindowMinutes, TimeUnit.MINUTES);
        }

        if (attempts > maxAttemptsPerHour) {
            logger.warn("Rate limit reached for {}", key);
            return true;
        }

        return false;
    }
}

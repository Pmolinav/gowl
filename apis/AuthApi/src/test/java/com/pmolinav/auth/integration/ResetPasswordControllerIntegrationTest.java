package com.pmolinav.auth.integration;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.userslib.model")
class ResetPasswordControllerIntegrationTest extends AbstractBaseTest {

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void cleanRedis() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    static RedisContainer redis = new RedisContainer(
            RedisContainer.DEFAULT_IMAGE_NAME.withTag("7.2.5"));

    static {
        redis.start();
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    // SEND CODE

    @Test
    void sendCodeOK() throws Exception {
        when(userClient.existsByEmail("user@example.com")).thenReturn(true);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        mockMvc.perform(post("/auth/send-code")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void sendCodeUserNotFound() throws Exception {
        when(userClient.existsByEmail("missing@example.com")).thenReturn(false);

        mockMvc.perform(post("/auth/send-code")
                        .param("requestUid", "otherRequestUid")
                        .param("email", "missing@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void sendCodeTooManyRequests() throws Exception {
        when(userClient.existsByEmail("user@example.com")).thenReturn(true);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/auth/send-code")
                            .param("requestUid", "someRequestUid" + i)
                            .param("email", "blocked@example.com"))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(post("/auth/send-code")
                        .param("requestUid", "anotherRequestUid")
                        .param("email", "blocked@example.com"))
                .andExpect(status().isTooManyRequests());
    }

    // VALIDATE CODE

    @Test
    void validateCodeOK() throws Exception {
        when(userClient.existsByEmail("user@example.com")).thenReturn(true);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        mockMvc.perform(post("/auth/send-code")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk());

        String key = "verification-code:user@example.com";
        String code = redisTemplate.opsForValue().get(key);

        mockMvc.perform(post("/auth/validate-code")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com")
                        .param("code", code))
                .andExpect(status().isOk());
    }

    @Test
    void validateCodeTooManyRequests() throws Exception {
        when(userClient.existsByEmail("user@example.com")).thenReturn(true);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        mockMvc.perform(post("/auth/send-code")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk());

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/auth/validate-code")
                            .param("requestUid", "someRequestUid" + i)
                            .param("email", "user@example.com")
                            .param("code", "1234"))
                    .andExpect(status().isBadRequest());
        }

        mockMvc.perform(post("/auth/validate-code")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com")
                        .param("code", "3210"))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void validateCodeBadRequest() throws Exception {
        mockMvc.perform(post("/auth/validate-code")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com")
                        .param("code", "user@example.com"))
                .andExpect(status().isBadRequest());
    }

    // UPDATE PASSWORD

    @Test
    void updatePasswordOk() throws Exception {
        when(userClient.existsByEmail("user@example.com")).thenReturn(true);
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        mockMvc.perform(post("/auth/send-code")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk());

        String key = "verification-code:user@example.com";
        String code = redisTemplate.opsForValue().get(key);

        mockMvc.perform(post("/auth/validate-code")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com")
                        .param("code", code))
                .andExpect(status().isOk());

        String tokenKey = "reset-token:user@example.com";
        String token = redisTemplate.opsForValue().get(tokenKey);

        mockMvc.perform(put("/auth/update-password")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com")
                        .param("newPassword", "someNewPassword")
                        .param("token", token))
                .andExpect(status().isOk());
    }

    @Test
    void updatePasswordBadRequest() throws Exception {
        mockMvc.perform(put("/auth/update-password")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com")
                        .param("newPassword", "someNewPassword"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePasswordUnauthorized() throws Exception {
        mockMvc.perform(put("/auth/update-password")
                        .param("requestUid", "someRequestUid")
                        .param("email", "user@example.com")
                        .param("newPassword", "someNewPassword")
                        .param("token", "invent"))
                .andExpect(status().isUnauthorized());
    }
}


package com.pmolinav.auth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.userslib.dto.LogoutDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.userslib.model")
class LogoutTokenControllerIntegrationTest extends AbstractBaseTest {

    @Test
    void logoutTokenHappyPath() throws Exception {
        andTokenIsInvalidated();
        Map<String, String> requestMap = Map.of("refreshToken", refreshToken);
        MvcResult result = mockMvc.perform(post("/auth/logout")
                        .param("requestUid", "someRequestUid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMap)))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, String> body = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(), Map.class);

        assertEquals("Session closed successfully", body.get("message"));
    }

    @Test
    void logoutAllTokensHappyPath() throws Exception {
        andAllTokensAreInvalidated();
        MvcResult result = mockMvc.perform(delete("/auth/logout/all")
                        .param("requestUid", "someRequestUid")
                        .param("username", username))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, String> body = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(), Map.class);

        assertEquals("All sessions closed successfully", body.get("message"));
    }

    @Test
    void logoutTokenNoRequestBodyBadRequest() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .param("requestUid", "someRequestUid"))
                .andExpect(status().isBadRequest());
    }

    private void andTokenIsInvalidated() {
        doNothing().when(this.userTokenClient).invalidateToken(any(LogoutDTO.class));
    }

    private void andAllTokensAreInvalidated() {
        doNothing().when(this.userTokenClient).invalidateAllTokens(anyString());
    }
}


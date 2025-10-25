package com.pmolinav.auth.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.userslib.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.userslib.model")
class RefreshTokenControllerIntegrationTest extends AbstractBaseTest {

    @Test
    void refreshTokenHappyPath() throws Exception {
        givenTokenExistsInClient(true);

        Map<String, String> requestMap = Map.of("refreshToken", refreshToken);
        MvcResult result = mockMvc.perform(post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMap)))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, String> body = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(), Map.class);
        String regex = "^[a-zA-Z0-9-_.]+$";

        assertThat(body.get("token")).matches(regex);
        assertThat(body.get("refreshToken")).matches(regex);
    }

    @Test
    void refreshTokenNoRequestBodyBadRequest() throws Exception {
        mockMvc.perform(post("/refresh"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshTokenDoesNotExistsUnauthorized() throws Exception {
        giveSomeValidRequest();
        givenTokenExistsInClient(false);

        UserDTO fakeUserRequest = new UserDTO();
        fakeUserRequest.setUsername("fakeUser");
        fakeUserRequest.setUsername("fakePass");

        mockMvc.perform(post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeUserRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refreshTokenUnauthorized() throws Exception {
        giveSomeValidRequest();

        Map<String, String> requestMap = Map.of("refreshToken", refreshToken);
        mockMvc.perform(post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMap)))
                .andExpect(status().isUnauthorized());
    }

    private void givenTokenExistsInClient(boolean exists) {
        when(this.userTokenClient.existsTokenForUser(anyString(), anyString()))
                .thenReturn(exists);
    }
}


package com.pmolinav.users.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.users.repositories.TokenRepository;
import com.pmolinav.userslib.dto.LogoutDTO;
import com.pmolinav.userslib.dto.UserTokenDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
//@EnableJpaRepositories("com.pmolinav.users.repositories")
@EntityScan("com.pmolinav.userslib.model")
class TokenControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void saveTokenHappyPath() throws Exception {
        UserTokenDTO requestDto = new UserTokenDTO("someUser", "someToken", "Device Agent",
                "192.168.1.1", LocalDate.now().plusDays(20).atStartOfDay());
        mockMvc.perform(post("/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void existsTokenForUsernameUnauthorized() throws Exception {
        givenSomePreviouslyStoredTokenDataWithId(657);

        LogoutDTO requestDto = new LogoutDTO("someUser", "inventedToken");

        mockMvc.perform(get("/tokens/exists/username/" + requestDto.getUsername()
                        + "?token=" + requestDto.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void existsTokenForUsernameDifferentUserUnauthorized() throws Exception {
        givenSomePreviouslyStoredTokenDataWithId(879);

        LogoutDTO requestDto = new LogoutDTO("otherUser", "someToken");

        mockMvc.perform(get("/tokens/exists/username/" + requestDto.getUsername()
                        + "?token=" + requestDto.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void existsTokenForUsernameHappyPath() throws Exception {
        givenSomePreviouslyStoredTokenDataWithId(346);

        LogoutDTO requestDto = new LogoutDTO("someUser", "someToken");

        mockMvc.perform(get("/tokens/exists/username/" + requestDto.getUsername()
                        + "?token=" + requestDto.getRefreshToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void invalidateTokenHappyPath() throws Exception {
        givenSomePreviouslyStoredTokenDataWithId(983);

        LogoutDTO requestDto = new LogoutDTO("someUser", "someToken");

        mockMvc.perform(post("/tokens/invalidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void invalidateAllTokensHappyPath() throws Exception {
        givenSomePreviouslyStoredTokenDataWithId(456);

        mockMvc.perform(delete("/tokens/invalidate/all?username=" + "someUser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
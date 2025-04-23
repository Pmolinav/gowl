package com.pmolinav.users.integration;

import com.pmolinav.userslib.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.userslib.model")
class LoginBOControllerIntegrationTest extends AbstractBaseTest {

    @Test
    void loginHappyPath() {
        String regex = "^Bearer\\s[a-zA-Z0-9-_.]+$";

        assertThat(authToken).matches(regex);
    }

    @Test
    void loginBadRequest() throws Exception {
        mockMvc.perform(post("/login"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginUnauthorized() throws Exception {
        giveSomeValidRequest();

        UserDTO fakeUserRequest = new UserDTO();
        fakeUserRequest.setUsername("fakeUser");
        fakeUserRequest.setUsername("fakePass");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeUserRequest)))
                .andExpect(status().isUnauthorized());
    }
}


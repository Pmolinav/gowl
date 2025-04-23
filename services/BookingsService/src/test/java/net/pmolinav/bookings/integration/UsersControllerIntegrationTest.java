package com.pmolinav.bookings.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.userslib.dto.Role;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import com.pmolinav.bookings.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@EnableJpaRepositories("com.pmolinav.bookings.repository")
@EntityScan("com.pmolinav.userslib.model")
class UsersControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findAllUsersNotFound() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllUsersHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithIds(1, 2, false, true, false);

        MvcResult result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        List<User> userResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<User>>() {
                });

        Assertions.assertEquals(2, userResponseList.size());
    }

    @Test
    void createUserHappyPath() throws Exception {
        UserDTO requestDto = new UserDTO("someUser", "somePassword", "someName",
                "some@email.com", Role.USER);

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody, matchesPattern("\\d+"));
    }

    @Test
    void findUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/users/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findUserByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithIds(3, 4, false, true, false);

        MvcResult result = mockMvc.perform(get("/users/3"))
                .andExpect(status().isOk())
                .andReturn();

        User userResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<User>() {
                });

        Assertions.assertEquals(3L, userResponse.getUserId());
    }

    @Test
    void deleteUserByIdNotFound() throws Exception {
        mockMvc.perform(delete("/users/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithIds(5, 6, false, true, false);

        mockMvc.perform(delete("/users/5"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/users/6"))
                .andExpect(status().isOk());
    }

}


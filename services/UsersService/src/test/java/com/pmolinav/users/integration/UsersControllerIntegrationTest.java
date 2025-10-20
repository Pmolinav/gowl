package com.pmolinav.users.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.users.auth.SpringSecurityConfig;
import com.pmolinav.users.repositories.UserRepository;
import com.pmolinav.userslib.dto.UpdatePasswordDTO;
import com.pmolinav.userslib.dto.UpdateUserDTO;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
//@EnableJpaRepositories("com.pmolinav.users.repositories")
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
        givenSomePreviouslyStoredDataWithId(1);

        MvcResult result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        List<User> userResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<User>>() {
                });

        assertEquals(2, userResponseList.size());
    }

    @Test
    void createUserHappyPath() throws Exception {
        UserDTO requestDto = new UserDTO("someUser", "somePassword", "someName",
                "some@email.com", LocalDate.of(1990, 10, 20), true);

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
        givenSomePreviouslyStoredDataWithId(3);

        MvcResult result = mockMvc.perform(get("/users/3"))
                .andExpect(status().isOk())
                .andReturn();

        User userResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<User>() {
                });

        assertEquals(3L, userResponse.getUserId());
    }

    @Test
    void findUserByUsernameNotFound() throws Exception {
        mockMvc.perform(get("/users/username/nonExistingUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findUserByUsernameHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithId(12);

        MvcResult result = mockMvc.perform(get("/users/username/someUser"))
                .andExpect(status().isOk())
                .andReturn();

        User userResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<User>() {
                });

        assertEquals(12L, userResponse.getUserId());
    }

    @Test
    void existsUserByEmailFalse() throws Exception {
        givenSomePreviouslyStoredDataWithId(399);

        MvcResult result = mockMvc.perform(get("/users/exists/email/" + "fake@email.com"))
                .andExpect(status().isOk())
                .andReturn();

        Boolean response = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Boolean>() {
                });

        assertFalse(response);
    }

    @Test
    void existsUserByEmailHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithId(399);

        MvcResult result = mockMvc.perform(get("/users/exists/email/" + "jane@example.com"))
                .andExpect(status().isOk())
                .andReturn();

        Boolean response = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Boolean>() {
                });

        assertTrue(response);
    }

    @Test
    void updateUserByIdNotFound() throws Exception {
        UpdateUserDTO requestDto = new UpdateUserDTO(null, "other@email.com",
                LocalDate.of(2004, 5, 8));

        mockMvc.perform(put("/users/798709")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithId(64);

        UpdateUserDTO requestDto = new UpdateUserDTO("Updated name", "updated@email.com", null);

        mockMvc.perform(put("/users/64")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        User user = userRepository.findById(64L).orElse(new User());
        assertEquals(requestDto.getName(), user.getName());
        assertEquals(requestDto.getEmail(), user.getEmail());
        assertNotNull(user.getBirthDate());
    }

    @Test
    void updateUserByUsernameNotFound() throws Exception {
        UpdateUserDTO requestDto = new UpdateUserDTO(null, "other@email.com",
                LocalDate.of(2004, 5, 8));

        mockMvc.perform(put("/users/username/invent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserByUsernameHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithId(33);

        UpdateUserDTO requestDto = new UpdateUserDTO("New name", "new@email.com",
                LocalDate.of(2025, 10, 17));

        mockMvc.perform(put("/users/username/someUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        User user = userRepository.findById(33L).orElse(new User());
        assertEquals(requestDto.getName(), user.getName());
        assertEquals(requestDto.getEmail(), user.getEmail());
        assertEquals(requestDto.getBirthDate(), user.getBirthDate());
    }

    @Test
    void updateUserPasswordByIdBadRequest() throws Exception {
        givenSomePreviouslyStoredDataWithId(179);

        UpdatePasswordDTO requestDto = new UpdatePasswordDTO("oldPass", "newPass");

        mockMvc.perform(put("/users/179/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserPasswordByIdNotFound() throws Exception {
        UpdatePasswordDTO requestDto = new UpdatePasswordDTO("pass", "newPass");

        mockMvc.perform(put("/users/7987029/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserPasswordByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithId(87);

        UpdatePasswordDTO requestDto = new UpdatePasswordDTO("somePassword", "newPassword");

        mockMvc.perform(put("/users/87/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        User user = userRepository.findById(87L).orElse(new User());
        assertTrue(SpringSecurityConfig.passwordEncoder().matches(requestDto.getNewPassword(), user.getPassword()));
    }

    @Test
    void updateUserPasswordByUsernameBadRequest() throws Exception {
        givenSomePreviouslyStoredDataWithId(664);

        UpdatePasswordDTO requestDto = new UpdatePasswordDTO("oldPass", "newPass");

        mockMvc.perform(put("/users/username/someUser/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserPasswordByUsernameNotFound() throws Exception {
        UpdatePasswordDTO requestDto = new UpdatePasswordDTO("pass", "newPass");

        mockMvc.perform(put("/users/username/fake/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserPasswordByUsernameHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithId(88);

        UpdatePasswordDTO requestDto = new UpdatePasswordDTO("somePassword", "newPassword");

        mockMvc.perform(put("/users/username/someUser/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        User user = userRepository.findById(88L).orElse(new User());
        assertTrue(SpringSecurityConfig.passwordEncoder().matches(requestDto.getNewPassword(), user.getPassword()));
    }

    @Test
    void updateUserPasswordByEmailBadRequest() throws Exception {
        givenSomePreviouslyStoredDataWithId(623);

        mockMvc.perform(put("/users/email/john@example.com/password?newPassword=newPass")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserByIdNotFound() throws Exception {
        mockMvc.perform(delete("/users/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithId(5);

        mockMvc.perform(delete("/users/5"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/users/6"))
                .andExpect(status().isOk());
    }

}


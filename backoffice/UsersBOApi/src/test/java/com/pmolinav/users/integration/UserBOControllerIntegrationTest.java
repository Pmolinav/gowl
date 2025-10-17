package com.pmolinav.users.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.userslib.model")
class UserBOControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<User> expectedUsers;

    @Test
    void findAllUsersInternalServerError() throws Exception {
        andFindAllUsersThrowsNonRetryableException();

        mockMvc.perform(get("/users?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAllUsersHappyPath() throws Exception {
        andFindAllUsersReturnedValidUsers();

        MvcResult result = mockMvc.perform(get("/users?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<User> userResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<User>>() {
                });

        Assertions.assertEquals(expectedUsers, userResponseList);
    }

    @Test
    void createUserServerError() throws Exception {
        andCreateUserThrowsNonRetryableException();

        UserDTO requestDto = new UserDTO("someUser", "somePassword", "someName",
                "some@email.com", LocalDate.of(1990, 10, 20), false);

        mockMvc.perform(post("/users?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createUserHappyPath() throws Exception {
        andCreateUserReturnedValidId();

        UserDTO requestDto = new UserDTO("someUser", "somePassword", "someName",
                "some@email.com", LocalDate.of(1990, 10, 20), false);

        MvcResult result = mockMvc.perform(post("/users?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody, matchesPattern("\\d+"));
    }

    @Test
    void findUserByIdServerError() throws Exception {
        andFindUserByIdThrowsNonRetryableException();

        mockMvc.perform(get("/users/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findUserByIdHappyPath() throws Exception {
        andFindUserByIdReturnedUser();

        MvcResult result = mockMvc.perform(get("/users/3?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        User userResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<User>() {
                });

        Assertions.assertEquals(expectedUsers.getFirst(), userResponse);
    }

    @Test
    void findUserByUsernameHappyPath() throws Exception {
        andFindUserByUsernameReturnedUser();

        MvcResult result = mockMvc.perform(get("/users/username/" + username + "?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        User userResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<User>() {
                });

        Assertions.assertEquals(expectedUsers.getFirst(), userResponse);
    }

    @Test
    void deleteUserByIdInternalServerError() throws Exception {
        andUserDeleteThrowsNonRetryableException();

        mockMvc.perform(delete("/users/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteUserByIdHappyPath() throws Exception {
        andUserIsDeletedOkOnClient();

        mockMvc.perform(delete("/users/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    private void andUserIsDeletedOkOnClient() {
        doNothing().when(this.userClient).deleteUser(anyLong());
    }

    private void andUserDeleteThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.userClient).deleteUser(anyLong());
    }

    private void andFindUserByIdReturnedUser() {
        this.expectedUsers = List.of(new User(1L, "someUser", "somePassword",
                "someName", "some@email.com", LocalDate.of(1990, 10, 20),
                new Date().getTime(), null, null));

        when(this.userClient.findUserById(anyLong())).thenReturn(this.expectedUsers.getFirst());
    }

    private void andFindUserByUsernameReturnedUser() {
        this.expectedUsers = List.of(new User(1L, "someUser", "somePassword",
                "someName", "some@email.com", LocalDate.of(1990, 10, 20),
                new Date().getTime(), null, null));

        when(this.userClient.findUserByUsername(anyString())).thenReturn(this.expectedUsers.getFirst());
    }

    private void andFindUserByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.userClient).findUserById(anyLong());
    }

    private void andCreateUserReturnedValidId() {
        when(this.userClient.createUser(any(UserDTO.class))).thenReturn(1L);
    }

    private void andCreateUserThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.userClient).createUser(any(UserDTO.class));
    }

    private void andFindAllUsersReturnedValidUsers() {
        this.expectedUsers = List.of(new User(1L, "someUser", "somePassword",
                "someName", "some@email.com", LocalDate.of(1990, 10, 20),
                new Date().getTime(), null, null));

        when(this.userClient.findAllUsers()).thenReturn(this.expectedUsers);
    }

    private void andFindAllUsersThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.userClient).findAllUsers();
    }
}


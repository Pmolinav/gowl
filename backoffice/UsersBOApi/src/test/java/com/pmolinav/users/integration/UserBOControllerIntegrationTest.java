package com.pmolinav.users.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.userslib.dto.UpdatePasswordDTO;
import com.pmolinav.userslib.dto.UpdateUserDTO;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
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

import static junit.framework.TestCase.assertEquals;
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

        assertEquals(expectedUsers, userResponseList);
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

        assertEquals(expectedUsers.getFirst(), userResponse);
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

        assertEquals(expectedUsers.getFirst(), userResponse);
    }

    @Test
    void updateUserByIdHappyPath() throws Exception {
        andUpdateUserByIdReturnedUser();

        UpdateUserDTO requestDto = new UpdateUserDTO("other name", null,
                LocalDate.of(1993, 11, 5));

        mockMvc.perform(put("/users/3?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserByUsernameHappyPath() throws Exception {
        andUpdateUserByUsernameReturnedUser();

        UpdateUserDTO requestDto = new UpdateUserDTO("someName", "some@email.com",
                LocalDate.of(1990, 10, 20));

        mockMvc.perform(put("/users/username/" + username + "?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserPasswordByIdHappyPath() throws Exception {
        andUpdateUserPasswordByIdReturnedUser();

        UpdatePasswordDTO requestDto = new UpdatePasswordDTO("oldPassword", "newPassword");

        mockMvc.perform(put("/users/3/password?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserPasswordByUsernameHappyPath() throws Exception {
        andUpdateUserPasswordByUsernameReturnedUser();

        UpdatePasswordDTO requestDto = new UpdatePasswordDTO("oldPassword123", "newPassword321");

        mockMvc.perform(put("/users/username/" + username + "/password" + "?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
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

    private void andUpdateUserByIdReturnedUser() {
        doNothing().when(this.userClient).updateUserById(anyLong(), any(UpdateUserDTO.class));
    }

    private void andUpdateUserByUsernameReturnedUser() {
        doNothing().when(this.userClient).updateUserByUsername(anyString(), any(UpdateUserDTO.class));
    }

    private void andUpdateUserPasswordByIdReturnedUser() {
        doNothing().when(this.userClient).updateUserPasswordById(anyLong(), any(UpdatePasswordDTO.class));
    }

    private void andUpdateUserPasswordByUsernameReturnedUser() {
        doNothing().when(this.userClient).updateUserPasswordByUsername(anyString(), any(UpdatePasswordDTO.class));
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


package com.pmolinav.auth.integration;

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
class UserControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<User> expectedUsers;

    @Test
    void createUserServerError() throws Exception {
        andCreateUserThrowsNonRetryableException();

        UserDTO requestDto = new UserDTO(username, "somePassword", "someName",
                "some@email.com", false);

        mockMvc.perform(post("/users?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createUserHappyPath() throws Exception {
        andCreateUserReturnedValidId();

        UserDTO requestDto = new UserDTO(username, "somePassword", "someName",
                "some@email.com", false);

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
        andFindUserByIdReturnedUser();
        andUserDeleteThrowsNonRetryableException();

        mockMvc.perform(delete("/users/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    //TODO: Why this test only work separately?
//    @Test
//    void deleteUserByIdHappyPath() throws Exception {
//        andFindUserByIdReturnedUser();
//        andUserIsDeletedOkOnClient();
//
//        mockMvc.perform(delete("/users/123?requestUid=" + requestUid)
//                        .header(HttpHeaders.AUTHORIZATION, authToken))
//                .andExpect(status().isOk());
//    }

    private void andUserIsDeletedOkOnClient() {
        doNothing().when(this.userClient).deleteUser(anyLong());
    }

    private void andUserDeleteThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.userClient).deleteUser(anyLong());
    }

    private void andFindUserByIdReturnedUser() {
        this.expectedUsers = List.of(new User(1L, username, "somePassword",
                "someName", "some@email.com", new Date().getTime(), null, null));

        when(this.userClient.findUserById(anyLong())).thenReturn(this.expectedUsers.getFirst());
    }

    private void andFindUserByUsernameReturnedUser() {
        this.expectedUsers = List.of(new User(1L, username, "somePassword",
                "someName", "some@email.com", new Date().getTime(), null, null));

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
}


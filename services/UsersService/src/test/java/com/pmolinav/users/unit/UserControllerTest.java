package com.pmolinav.users.unit;

import com.pmolinav.users.exceptions.InternalServerErrorException;
import com.pmolinav.users.exceptions.NotFoundException;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserControllerTest extends BaseUnitTest {

    UserDTO userDTO;
    List<User> expectedUsers;
    ResponseEntity<?> result;

    /* FIND ALL USERS */
    @Test
    void findAllUsersHappyPath() {
        whenFindAllUsersInServiceReturnedValidUsers();
        andFindAllUsersIsCalledInController();
        thenVerifyFindAllUsersHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedUsers));
    }

    @Test
    void findAllUsersNotFound() {
        whenFindAllUsersInServiceThrowsNotFoundException();
        andFindAllUsersIsCalledInController();
        thenVerifyFindAllUsersHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllUsersServerError() {
        whenFindAllUsersInServiceThrowsServerException();
        andFindAllUsersIsCalledInController();
        thenVerifyFindAllUsersHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CREATE USER */
    @Test
    void createUserHappyPath() {
        givenValidUserDTOForRequest("someUsername", "somePassword", "someName", "some@email.com", true);
        whenCreateUserInServiceReturnedAValidUser();
        andCreateUserIsCalledInController();
        thenVerifyCreateUserHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsStringIs(String.valueOf(1));
    }

    @Test
    void createUserServerError() {
        givenValidUserDTOForRequest("someUsername", "somePassword", "someName", "some@email.com", true);
        whenCreateUserInServiceThrowsServerException();
        andCreateUserIsCalledInController();
        thenVerifyCreateUserHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND USER BY ID */
    @Test
    void findUserByIdHappyPath() {
        whenFindUserByIdInServiceReturnedValidUser();
        andFindUserByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedUsers.getFirst()));
    }

    @Test
    void findUserByIdNotFound() {
        whenFindUserByIdInServiceThrowsNotFoundException();
        andFindUserByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findUserByIdServerError() {
        whenFindUserByIdInServiceThrowsServerException();
        andFindUserByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND USER BY USERNAME */
    @Test
    void findUserByUsernameHappyPath() {
        whenFindUserByUsernameInServiceReturnedValidUser();
        andFindUserByUsernameIsCalledInController();
        thenVerifyFindByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedUsers.getFirst()));
    }

    @Test
    void findUserUsernameUsernameNotFound() {
        whenFindUserByUsernameInServiceThrowsNotFoundException();
        andFindUserByUsernameIsCalledInController();
        thenVerifyFindByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findUserUsernameUsernameServerError() {
        whenFindUserByUsernameInServiceThrowsServerException();
        andFindUserByUsernameIsCalledInController();
        thenVerifyFindByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE USER */
    @Test
    void deleteUserHappyPath() {
        whenDeleteUserInServiceIsOk();
        andDeleteUserIsCalledInController();
        thenVerifyDeleteUserHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteUserNotFound() {
        whenDeleteUserInServiceThrowsNotFoundException();
        andDeleteUserIsCalledInController();
        thenVerifyDeleteUserHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteUserServerError() {
        whenDeleteUserInServiceThrowsServerException();
        andDeleteUserIsCalledInController();
        thenVerifyDeleteUserHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void givenValidUserDTOForRequest(String username, String password, String name, String email, boolean isAdmin) {
        userDTO = new UserDTO(username, password, name, email, isAdmin);
    }

    private void whenFindAllUsersInServiceReturnedValidUsers() {
        expectedUsers = List.of(
                new User(1L, "someUser", "somePassword", "someName",
                        "some@email.com", 1L, new Date().getTime(), null),
                new User(2L, "someUser", "somePassword", "someName",
                        "some@email.com", new Date().getTime(), null, null));

        when(userServiceMock.findAllUsers()).thenReturn(expectedUsers);
    }

    private void whenFindAllUsersInServiceThrowsNotFoundException() {
        when(userServiceMock.findAllUsers()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllUsersInServiceThrowsServerException() {
        when(userServiceMock.findAllUsers())
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateUserInServiceReturnedAValidUser() {
        when(userServiceMock.createUser(any())).thenReturn(new User(1L, "someUser", "somePassword", "someName",
                "some@email.com", new Date().getTime(), null, null));
    }

    private void whenCreateUserInServiceThrowsServerException() {
        when(userServiceMock.createUser(any(UserDTO.class)))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindUserByIdInServiceReturnedValidUser() {
        expectedUsers = List.of(
                new User(1L, "someUser", "somePassword", "someName",
                        "some@email.com", new Date().getTime(), null, null));

        when(userServiceMock.findById(1L)).thenReturn(expectedUsers.getFirst());
    }

    private void whenFindUserByIdInServiceThrowsNotFoundException() {
        when(userServiceMock.findById(1L)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindUserByIdInServiceThrowsServerException() {
        when(userServiceMock.findById(1L))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindUserByUsernameInServiceReturnedValidUser() {
        expectedUsers = List.of(
                new User(1L, "someUser", "somePassword", "someName",
                        "some@email.com", new Date().getTime(), null, null));

        when(userServiceMock.findByUsername(eq(expectedUsers.getFirst().getUsername())))
                .thenReturn(expectedUsers.getFirst());
    }

    private void whenFindUserByUsernameInServiceThrowsNotFoundException() {
        when(userServiceMock.findByUsername(anyString())).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindUserByUsernameInServiceThrowsServerException() {
        when(userServiceMock.findByUsername(anyString()))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenDeleteUserInServiceIsOk() {
        doNothing().when(userServiceMock).deleteUser(anyLong());
    }

    private void whenDeleteUserInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(userServiceMock)
                .deleteUser(anyLong());
    }

    private void whenDeleteUserInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(userServiceMock)
                .deleteUser(anyLong());
    }

    private void andFindAllUsersIsCalledInController() {
        result = userController.findAllUsers();
    }

    private void andFindUserByIdIsCalledInController() {
        result = userController.findUserById(1L);
    }

    private void andFindUserByUsernameIsCalledInController() {
        result = userController.findUserByUsername("someUser");
    }

    private void andCreateUserIsCalledInController() {
        result = userController.createUser(userDTO);
    }

    private void andDeleteUserIsCalledInController() {
        result = userController.deleteUser(1L);
    }

    private void thenVerifyFindAllUsersHasBeenCalledInService() {
        verify(userServiceMock, times(1)).findAllUsers();
    }

    private void thenVerifyCreateUserHasBeenCalledInService() {
        verify(userServiceMock, times(1)).createUser(any(UserDTO.class));
    }

    private void thenVerifyFindByIdHasBeenCalledInService() {
        verify(userServiceMock, times(1)).findById(anyLong());
    }

    private void thenVerifyFindByUsernameHasBeenCalledInService() {
        verify(userServiceMock, times(1)).findByUsername(anyString());
    }

    private void thenVerifyDeleteUserHasBeenCalledInService() {
        verify(userServiceMock, times(1)).deleteUser(anyLong());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, String.valueOf(result.getBody()));
    }
}

package com.pmolinav.leagues.units;

import com.pmolinav.leagues.exceptions.CustomStatusException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.UserDTO;
import com.pmolinav.leagueslib.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class LeaguesBOControllerTest extends BaseUnitTest {

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
    void createUserBadRequest() {
        givenValidUserDTOForRequest("someUsername", "somePassword", "someName", "some@email.com", true);
        whenCreateUserInServiceReturnedAValidUser();
        andCreateUserIsCalledInControllerWithBindingResultErrors();
        thenReceivedStatusCodeIs(HttpStatus.BAD_REQUEST);
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
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedUsers.get(0)));
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

    /* FIND USER BY USERNAME - NOT ALLOWED IN CONTROLLER. ONLY INTERNAL REQUEST*/

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
                        "some@email.com", new Date(1L), null, null),
                new User(2L, "someUser", "somePassword", "someName",
                        "some@email.com", new Date(1L), null, null));

        when(leaguesBOServiceMock.findAllUsers()).thenReturn(expectedUsers);
    }

    private void whenFindAllUsersInServiceThrowsNotFoundException() {
        when(leaguesBOServiceMock.findAllUsers()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllUsersInServiceThrowsServerException() {
        when(leaguesBOServiceMock.findAllUsers())
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenCreateUserInServiceReturnedAValidUser() {
        when(leaguesBOServiceMock.createUser(any())).thenReturn(1L);
    }

    private void whenCreateUserInServiceThrowsServerException() {
        when(leaguesBOServiceMock.createUser(any(UserDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindUserByIdInServiceReturnedValidUser() {
        expectedUsers = List.of(
                new User(1L, "someUser", "somePassword", "someName",
                        "some@email.com", new Date(1L), null, null));

        when(leaguesBOServiceMock.findUserById(1L)).thenReturn(expectedUsers.get(0));
    }

    private void whenFindUserByIdInServiceThrowsNotFoundException() {
        when(leaguesBOServiceMock.findUserById(1L)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindUserByIdInServiceThrowsServerException() {
        when(leaguesBOServiceMock.findUserById(1L))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindUserByUsernameInServiceReturnedValidUser() {
        expectedUsers = List.of(
                new User(1L, "someUser", "somePassword", "someName",
                        "some@email.com", new Date(1L), null, null));

        when(leaguesBOServiceMock.findUserByUsername(eq(expectedUsers.get(0).getUsername()))).thenReturn(expectedUsers.get(0));
    }

    private void whenDeleteUserInServiceIsOk() {
        doNothing().when(leaguesBOServiceMock).deleteUser(anyLong());
    }

    private void whenDeleteUserInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguesBOServiceMock)
                .deleteUser(anyLong());
    }

    private void whenDeleteUserInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(leaguesBOServiceMock)
                .deleteUser(anyLong());
    }

    private void andFindAllUsersIsCalledInController() {
        result = leaguesBOController.findAllUsers(this.requestUid);
    }

    private void andFindUserByIdIsCalledInController() {
        result = leaguesBOController.getUserById(this.requestUid, 1L);
    }

    private void andCreateUserIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = leaguesBOController.createUser(this.requestUid, userDTO, bindingResult);
    }

    private void andCreateUserIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("userDTO", "username", "Username is mandatory.")
        ));

        result = leaguesBOController.createUser(this.requestUid, userDTO, bindingResult);
    }

    private void andDeleteUserIsCalledInController() {
        result = leaguesBOController.deleteUser(this.requestUid, 1L);
    }

    private void thenVerifyFindAllUsersHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).findAllUsers();
    }

    private void thenVerifyCreateUserHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).createUser(any(UserDTO.class));
    }

    private void thenVerifyFindByIdHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).findUserById(anyLong());
    }

    private void thenVerifyDeleteUserHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).deleteUser(anyLong());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, String.valueOf(result.getBody()));
    }
}

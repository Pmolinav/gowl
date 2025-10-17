package com.pmolinav.users.units;

import com.pmolinav.users.exceptions.CustomStatusException;
import com.pmolinav.users.exceptions.NotFoundException;
import com.pmolinav.userslib.dto.UpdatePasswordDTO;
import com.pmolinav.userslib.dto.UpdateUserDTO;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserBOControllerTest extends BaseUnitTest {

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
    void findUserByUsernameNotFound() {
        whenFindUserByUsernameInServiceThrowsNotFoundException();
        andFindUserByUsernameIsCalledInController();
        thenVerifyFindByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findUserByUsernameServerError() {
        whenFindUserByUsernameInServiceThrowsServerException();
        andFindUserByUsernameIsCalledInController();
        thenVerifyFindByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* UPDATE USER BY ID */
    @Test
    void updateUserByIdHappyPath() {
        whenUpdateUserByIdInServiceReturnedValidUser();
        andUpdateUserByIdIsCalledInController();
        thenVerifyUpdateByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void updateUserByIdNotFound() {
        whenUpdateUserByIdInServiceThrowsNotFoundException();
        andUpdateUserByIdIsCalledInController();
        thenVerifyUpdateByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateUserByIdServerError() {
        whenUpdateUserByIdInServiceThrowsServerException();
        andUpdateUserByIdIsCalledInController();
        thenVerifyUpdateByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* UPDATE USER BY USERNAME */
    @Test
    void updateUserByUsernameHappyPath() {
        whenUpdateUserByUsernameInServiceReturnedValidUser();
        andUpdateUserByUsernameIsCalledInController();
        thenVerifyUpdateByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void updateUserByUsernameNotFound() {
        whenUpdateUserByUsernameInServiceThrowsNotFoundException();
        andUpdateUserByUsernameIsCalledInController();
        thenVerifyUpdateByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateUserByUsernameServerError() {
        whenUpdateUserByUsernameInServiceThrowsServerException();
        andUpdateUserByUsernameIsCalledInController();
        thenVerifyUpdateByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* UPDATE USER PASSWORD BY ID */
    @Test
    void updateUserPasswordByIdHappyPath() {
        whenUpdateUserPasswordByIdInServiceReturnedValidUser();
        andUpdateUserPasswordByIdIsCalledInController();
        thenVerifyUpdatePasswordByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void updateUserPasswordByIdNotFound() {
        whenUpdateUserPasswordByIdInServiceThrowsNotFoundException();
        andUpdateUserPasswordByIdIsCalledInController();
        thenVerifyUpdatePasswordByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateUserPasswordByIdServerError() {
        whenUpdateUserPasswordByIdInServiceThrowsServerException();
        andUpdateUserPasswordByIdIsCalledInController();
        thenVerifyUpdatePasswordByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* UPDATE USER PASSWORD BY USERNAME */
    @Test
    void updateUserPasswordByUsernameHappyPath() {
        whenUpdateUserPasswordByUsernameInServiceReturnedValidUser();
        andUpdateUserPasswordByUsernameIsCalledInController();
        thenVerifyUpdatePasswordByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void updateUserPasswordByUsernameNotFound() {
        whenUpdateUserPasswordByUsernameInServiceThrowsNotFoundException();
        andUpdateUserPasswordByUsernameIsCalledInController();
        thenVerifyUpdatePasswordByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateUserPasswordByUsernameServerError() {
        whenUpdateUserPasswordByUsernameInServiceThrowsServerException();
        andUpdateUserPasswordByUsernameIsCalledInController();
        thenVerifyUpdatePasswordByUsernameHasBeenCalledInService();
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

    private void givenValidUserDTOForRequest(String username, String password, String name, String email, boolean admin) {
        userDTO = new UserDTO(username, password, name, email, LocalDate.of(1990, 10, 20), admin);
    }

    private void whenFindAllUsersInServiceReturnedValidUsers() {
        expectedUsers = List.of(
                new User(1L, "someUser", "somePassword", "someName",
                        "some@email.com", LocalDate.of(1990, 10, 20),
                        1L, null, null),
                new User(2L, "someUser", "somePassword", "someName",
                        "some@email.com", LocalDate.of(1990, 10, 20),
                        1L, null, null));

        when(userBOServiceMock.findAllUsers()).thenReturn(expectedUsers);
    }

    private void whenFindAllUsersInServiceThrowsNotFoundException() {
        when(userBOServiceMock.findAllUsers()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllUsersInServiceThrowsServerException() {
        when(userBOServiceMock.findAllUsers())
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenCreateUserInServiceReturnedAValidUser() {
        when(userBOServiceMock.createUser(any())).thenReturn(1L);
    }

    private void whenCreateUserInServiceThrowsServerException() {
        when(userBOServiceMock.createUser(any(UserDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindUserByIdInServiceReturnedValidUser() {
        expectedUsers = List.of(
                new User(1L, "someUser", "somePassword", "someName", "some@email.com",
                        LocalDate.of(1990, 10, 20), 1L, null, null));

        when(userBOServiceMock.findUserById(1L)).thenReturn(expectedUsers.getFirst());
    }

    private void whenFindUserByIdInServiceThrowsNotFoundException() {
        when(userBOServiceMock.findUserById(1L)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindUserByIdInServiceThrowsServerException() {
        when(userBOServiceMock.findUserById(1L))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindUserByUsernameInServiceReturnedValidUser() {
        expectedUsers = List.of(
                new User(1L, "someUser", "somePassword", "someName", "some@email.com",
                        LocalDate.of(1990, 10, 20), 1L, null, null));

        when(userBOServiceMock.findUserByUsername(eq(expectedUsers.getFirst().getUsername()))).thenReturn(expectedUsers.getFirst());
    }

    private void whenFindUserByUsernameInServiceThrowsNotFoundException() {
        when(userBOServiceMock.findUserByUsername("someUser")).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindUserByUsernameInServiceThrowsServerException() {
        when(userBOServiceMock.findUserByUsername("someUser"))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenUpdateUserByIdInServiceReturnedValidUser() {
        doNothing().when(userBOServiceMock)
                .updateUserById(anyLong(), any(UpdateUserDTO.class));
    }

    private void whenUpdateUserByIdInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(userBOServiceMock)
                .updateUserById(anyLong(), any(UpdateUserDTO.class));
    }

    private void whenUpdateUserByIdInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(userBOServiceMock)
                .updateUserById(anyLong(), any(UpdateUserDTO.class));
    }

    private void whenUpdateUserByUsernameInServiceReturnedValidUser() {
        doNothing().when(userBOServiceMock)
                .updateUserPasswordByUsername(anyString(), any(UpdatePasswordDTO.class));
    }

    private void whenUpdateUserByUsernameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(userBOServiceMock)
                .updateUserByUsername(anyString(), any(UpdateUserDTO.class));
    }

    private void whenUpdateUserByUsernameInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(userBOServiceMock)
                .updateUserByUsername(anyString(), any(UpdateUserDTO.class));
    }

    private void whenUpdateUserPasswordByIdInServiceReturnedValidUser() {
        doNothing().when(userBOServiceMock)
                .updateUserPasswordById(anyLong(), any(UpdatePasswordDTO.class));
    }

    private void whenUpdateUserPasswordByIdInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(userBOServiceMock)
                .updateUserPasswordById(anyLong(), any(UpdatePasswordDTO.class));
    }

    private void whenUpdateUserPasswordByIdInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(userBOServiceMock)
                .updateUserPasswordById(anyLong(), any(UpdatePasswordDTO.class));
    }

    private void whenUpdateUserPasswordByUsernameInServiceReturnedValidUser() {
        doNothing().when(userBOServiceMock)
                .updateUserPasswordByUsername(anyString(), any(UpdatePasswordDTO.class));
    }

    private void whenUpdateUserPasswordByUsernameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(userBOServiceMock)
                .updateUserPasswordByUsername(anyString(), any(UpdatePasswordDTO.class));
    }

    private void whenUpdateUserPasswordByUsernameInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(userBOServiceMock)
                .updateUserPasswordByUsername(anyString(), any(UpdatePasswordDTO.class));
    }

    private void whenDeleteUserInServiceIsOk() {
        doNothing().when(userBOServiceMock).deleteUser(anyLong());
    }

    private void whenDeleteUserInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(userBOServiceMock)
                .deleteUser(anyLong());
    }

    private void whenDeleteUserInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(userBOServiceMock)
                .deleteUser(anyLong());
    }

    private void andFindAllUsersIsCalledInController() {
        result = userBOController.findAllUsers(this.requestUid);
    }

    private void andFindUserByIdIsCalledInController() {
        result = userBOController.getUserById(this.requestUid, 1L);
    }

    private void andFindUserByUsernameIsCalledInController() {
        result = userBOController.getUserByUsername(this.requestUid, "someUser");
    }

    private void andUpdateUserByIdIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = userBOController.updateUserById(this.requestUid, 1L,
                new UpdateUserDTO("new name", "new@email.com", null), bindingResult);
    }

    private void andUpdateUserByUsernameIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = userBOController.updateUserByUsername(this.requestUid, "someUser",
                new UpdateUserDTO("new name", "new@email.com", null), bindingResult);
    }

    private void andUpdateUserPasswordByIdIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = userBOController.updateUserPasswordById(this.requestUid, 1L,
                new UpdatePasswordDTO("oldPass", "newPass"), bindingResult);
    }

    private void andUpdateUserPasswordByUsernameIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = userBOController.updateUserPasswordByUsername(this.requestUid, "someUser",
                new UpdatePasswordDTO("oldPass", "newPass"), bindingResult);
    }

    private void andCreateUserIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = userBOController.createUser(this.requestUid, userDTO, bindingResult);
    }

    private void andCreateUserIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("userDTO", "username", "Username is mandatory.")
        ));

        result = userBOController.createUser(this.requestUid, userDTO, bindingResult);
    }

    private void andDeleteUserIsCalledInController() {
        result = userBOController.deleteUser(this.requestUid, 1L);
    }

    private void thenVerifyFindAllUsersHasBeenCalledInService() {
        verify(userBOServiceMock, times(1)).findAllUsers();
    }

    private void thenVerifyCreateUserHasBeenCalledInService() {
        verify(userBOServiceMock, times(1)).createUser(any(UserDTO.class));
    }

    private void thenVerifyFindByIdHasBeenCalledInService() {
        verify(userBOServiceMock, times(1)).findUserById(anyLong());
    }

    private void thenVerifyFindByUsernameHasBeenCalledInService() {
        verify(userBOServiceMock, times(1)).findUserByUsername(anyString());
    }

    private void thenVerifyUpdateByIdHasBeenCalledInService() {
        verify(userBOServiceMock, times(1)).updateUserById(anyLong(), any(UpdateUserDTO.class));
    }

    private void thenVerifyUpdateByUsernameHasBeenCalledInService() {
        verify(userBOServiceMock, times(1)).updateUserByUsername(anyString(), any(UpdateUserDTO.class));
    }

    private void thenVerifyUpdatePasswordByIdHasBeenCalledInService() {
        verify(userBOServiceMock, times(1)).updateUserPasswordById(anyLong(), any(UpdatePasswordDTO.class));
    }

    private void thenVerifyUpdatePasswordByUsernameHasBeenCalledInService() {
        verify(userBOServiceMock, times(1)).updateUserPasswordByUsername(anyString(), any(UpdatePasswordDTO.class));
    }

    private void thenVerifyDeleteUserHasBeenCalledInService() {
        verify(userBOServiceMock, times(1)).deleteUser(anyLong());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, String.valueOf(result.getBody()));
    }
}

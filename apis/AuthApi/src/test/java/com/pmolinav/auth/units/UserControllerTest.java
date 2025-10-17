package com.pmolinav.auth.units;

import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.exceptions.NotFoundException;
import com.pmolinav.userslib.dto.UpdatePasswordDTO;
import com.pmolinav.userslib.dto.UpdateUserDTO;
import com.pmolinav.userslib.dto.UserPublicDTO;
import com.pmolinav.userslib.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserControllerTest extends BaseUnitTest {

    UserPublicDTO userPublicDTO;
    List<User> expectedUsers;
    ResponseEntity<?> result;

    /* CREATE USER */
    @Test
    void createUserHappyPath() {
        givenValidUserDTOForRequest("someUsername", "somePassword", "someName", "some@email.com");
        whenCreateUserInServiceReturnedAValidUser();
        andCreateUserIsCalledInController();
        thenVerifyCreateUserHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsStringIs(String.valueOf(1));
    }

    @Test
    void createUserBadRequest() {
        givenValidUserDTOForRequest("someUsername", "somePassword", "someName", "some@email.com");
        whenCreateUserInServiceReturnedAValidUser();
        andCreateUserIsCalledInControllerWithBindingResultErrors();
        thenReceivedStatusCodeIs(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createUserServerError() {
        givenValidUserDTOForRequest("someUsername", "somePassword", "someName", "some@email.com");
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
        givenMockedSecurityContextWithUser("someUsername");
        whenFindUserByUsernameInServiceReturnedValidUser();
        andFindUserByUsernameIsCalledInController();
        thenVerifyFindByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedUsers.getFirst()));
    }

    @Test
    void findUserByUsernameNotFound() {
        givenMockedSecurityContextWithUser("someUsername");
        whenFindUserByUsernameInServiceThrowsNotFoundException();
        andFindUserByUsernameIsCalledInController();
        thenVerifyFindByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findUserByUsernameServerError() {
        givenMockedSecurityContextWithUser("someUsername");
        whenFindUserByUsernameInServiceThrowsServerException();
        andFindUserByUsernameIsCalledInController();
        thenVerifyFindByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* UPDATE USER BY USERNAME */
    @Test
    void updateUserByUsernameHappyPath() {
        givenMockedSecurityContextWithUser("someUsername");
        whenUpdateUserByUsernameInServiceIsOk();
        andUpdateUserByUsernameIsCalledInController();
        thenVerifyUpdateByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void updateUserByUsernameNotFound() {
        givenMockedSecurityContextWithUser("someUsername");
        whenUpdateUserByUsernameInServiceThrowsNotFoundException();
        andUpdateUserByUsernameIsCalledInController();
        thenVerifyUpdateByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateUserByUsernameServerError() {
        givenMockedSecurityContextWithUser("someUsername");
        whenUpdateUserByUsernameInServiceThrowsServerException();
        andUpdateUserByUsernameIsCalledInController();
        thenVerifyUpdateByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* UPDATE USER PASSWORD BY USERNAME */
    @Test
    void updateUserPasswordByUsernameHappyPath() {
        givenMockedSecurityContextWithUser("someUsername");
        whenUpdateUserPasswordByUsernameInServiceIsOk();
        andUpdateUserPasswordByUsernameIsCalledInController();
        thenVerifyUpdatePasswordByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void updateUserPasswordByUsernameNotFound() {
        givenMockedSecurityContextWithUser("someUsername");
        whenUpdateUserPasswordByUsernameInServiceThrowsNotFoundException();
        andUpdateUserPasswordByUsernameIsCalledInController();
        thenVerifyUpdatePasswordByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateUserPasswordByUsernameServerError() {
        givenMockedSecurityContextWithUser("someUsername");
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

    private void givenValidUserDTOForRequest(String username, String password, String name, String email) {
        userPublicDTO = new UserPublicDTO(username, password, name, email, LocalDate.of(1990, 10, 20));
    }

    private void givenMockedSecurityContextWithUser(String username) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(username, null, List.of());

        SecurityContext context = Mockito.mock(SecurityContext.class);

        Mockito.when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    private void whenCreateUserInServiceReturnedAValidUser() {
        when(userServiceMock.createUser(any())).thenReturn(1L);
    }

    private void whenCreateUserInServiceThrowsServerException() {
        when(userServiceMock.createUser(any(UserPublicDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindUserByIdInServiceReturnedValidUser() {
        expectedUsers = List.of(
                new User(1L, "someUsername", "somePassword", "someName", "some@email.com",
                        LocalDate.of(1990, 10, 20), 1L, null, null));

        when(userServiceMock.findUserById(1L)).thenReturn(expectedUsers.getFirst());
    }

    private void whenFindUserByIdInServiceThrowsNotFoundException() {
        when(userServiceMock.findUserById(1L)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindUserByIdInServiceThrowsServerException() {
        when(userServiceMock.findUserById(1L))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindUserByUsernameInServiceReturnedValidUser() {
        expectedUsers = List.of(
                new User(1L, "someUsername", "somePassword", "someName", "some@email.com",
                        LocalDate.of(1990, 10, 20), 1L, 1L, null));

        when(userServiceMock.findUserByUsername(anyString())).thenReturn(expectedUsers.getFirst());
    }

    private void whenFindUserByUsernameInServiceThrowsNotFoundException() {
        when(userServiceMock.findUserByUsername("someUsername")).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindUserByUsernameInServiceThrowsServerException() {
        when(userServiceMock.findUserByUsername("someUsername"))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenUpdateUserByUsernameInServiceIsOk() {
        doNothing().when(userServiceMock)
                .updateUserByUsername(anyString(), any(UpdateUserDTO.class));
    }

    private void whenUpdateUserByUsernameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(userServiceMock)
                .updateUserByUsername(anyString(), any(UpdateUserDTO.class));
    }

    private void whenUpdateUserByUsernameInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(userServiceMock)
                .updateUserByUsername(anyString(), any(UpdateUserDTO.class));
    }

    private void whenUpdateUserPasswordByUsernameInServiceIsOk() {
        doNothing().when(userServiceMock)
                .updateUserPasswordByUsername(anyString(), any(UpdatePasswordDTO.class));
    }

    private void whenUpdateUserPasswordByUsernameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(userServiceMock)
                .updateUserPasswordByUsername(anyString(), any(UpdatePasswordDTO.class));
    }

    private void whenUpdateUserPasswordByUsernameInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(userServiceMock)
                .updateUserPasswordByUsername(anyString(), any(UpdatePasswordDTO.class));
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
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(userServiceMock)
                .deleteUser(anyLong());
    }

    private void andFindUserByIdIsCalledInController() {
        result = userController.findUserById(this.requestUid, 1L);
    }

    private void andFindUserByUsernameIsCalledInController() {
        result = userController.findUserByUsername(this.requestUid, "someUsername");
    }

    private void andUpdateUserByUsernameIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = userController.updateUserByUsername(this.requestUid, "someUsername",
                new UpdateUserDTO("new name", "new@email.com", null), bindingResult);
    }

    private void andUpdateUserPasswordByUsernameIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = userController.updateUserPasswordByUsername(this.requestUid, "someUsername",
                new UpdatePasswordDTO("oldPass", "newPass"), bindingResult);
    }

    private void andCreateUserIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = userController.createUser(this.requestUid, userPublicDTO, bindingResult);
    }

    private void andCreateUserIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("userDTO", "username", "Username is mandatory.")
        ));

        result = userController.createUser(this.requestUid, userPublicDTO, bindingResult);
    }

    private void andDeleteUserIsCalledInController() {
        result = userController.deleteUser(this.requestUid, 1L);
    }

    private void thenVerifyCreateUserHasBeenCalledInService() {
        verify(userServiceMock, times(1)).createUser(any(UserPublicDTO.class));
    }

    private void thenVerifyFindByIdHasBeenCalledInService() {
        verify(userServiceMock, times(1)).findUserById(anyLong());
    }

    private void thenVerifyFindByUsernameHasBeenCalledInService() {
        verify(userServiceMock, times(1)).findUserByUsername(anyString());
    }

    private void thenVerifyUpdateByUsernameHasBeenCalledInService() {
        verify(userServiceMock, times(1))
                .updateUserByUsername(anyString(), any(UpdateUserDTO.class));
    }

    private void thenVerifyUpdatePasswordByUsernameHasBeenCalledInService() {
        verify(userServiceMock, times(1))
                .updateUserPasswordByUsername(anyString(), any(UpdatePasswordDTO.class));
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

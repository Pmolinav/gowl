package com.pmolinav.auth.units;

import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.models.request.Role;
import com.pmolinav.userslib.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginControllerTest extends BaseUnitTest {

    ResponseEntity<?> result;

    /* LOGIN */
    @Test
    void loginHappyPath() {
        returnValidTokenConfigValues();
        doNothingWhenAuthenticateIsCalled();
        doNothingWhenUserTokenServiceIsCalled();
        andLoginsIsCalledInController();
        thenVerifyAuthenticationHasBeenCalledInManager();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyContains("Welcome someUsername");
    }

    @Test
    void findAllUsersUnauthorized() {
        whenAuthenticateThrowsBadCredentialsException();
        andLoginsIsCalledInController();
        thenVerifyAuthenticationHasBeenCalledInManager();
        thenReceivedStatusCodeIs(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void findAllUsersServerError() {
        whenAuthenticateThrowsUnexpectedException();
        andLoginsIsCalledInController();
        thenVerifyAuthenticationHasBeenCalledInManager();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void returnValidTokenConfigValues() {
        when(tokenConfig.getSecret()).thenReturn("c7eD5hYnJnVr3uFTh5WTG2XKj6qbBszvuztf8WbCcJY");
        when(tokenConfig.getValiditySeconds()).thenReturn(12345L);
    }

    private void doNothingWhenAuthenticateIsCalled() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "someUsername",
                "somePassword",
                Collections.singletonList(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
        );
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
    }

    private void doNothingWhenUserTokenServiceIsCalled() {
        doNothing()
                .when(userTokenAsyncServiceMock)
                .saveUserTokenAsync(anyString(), nullable(String.class), anyString(), nullable(String.class),
                        nullable(String.class), any(LocalDateTime.class));
    }

    private void whenAuthenticateThrowsBadCredentialsException() {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));
    }

    private void whenAuthenticateThrowsUnexpectedException() {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void andLoginsIsCalledInController() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDTO user = new UserDTO();
        user.setUsername("someUsername");
        user.setPassword("somePassword");
        result = loginController.login(user, request);
    }

    private void thenVerifyAuthenticationHasBeenCalledInManager() {
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyContains(String value) {
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertTrue(result.getBody().toString().contains(value));
    }
}

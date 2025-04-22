package net.pmolinav.configuration.units;

import net.pmolinav.bookingslib.exception.CustomStatusException;
import net.pmolinav.configuration.security.AuthCredentials;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginBOControllerTest extends BaseUnitTest {

    ResponseEntity<?> result;

    /* LOGIN */
    @Test
    void loginHappyPath() {
        doNothingWhenAuthenticateIsCalled();
        andLoginsIsCalledInController();
        thenVerifyAuthenticationHasBeenCalledInManager();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyIsEmpty();
        thenReceivedResponseAuthHeaderMatchesWithToken();
    }

    @Test
    void findAllUsersUnauthorized() {
        whenAuthenticateThrowsBadCredentialsException();
        andLoginsIsCalledInController();
        thenVerifyAuthenticationHasBeenCalledInManager();
        thenReceivedStatusCodeIs(HttpStatus.UNAUTHORIZED);
        thenReceivedResponseBodyIsEmpty();
    }

    @Test
    void findAllUsersServerError() {
        whenAuthenticateThrowsUnexpectedException();
        andLoginsIsCalledInController();
        thenVerifyAuthenticationHasBeenCalledInManager();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
        thenReceivedResponseBodyIsEmpty();
    }

    private void doNothingWhenAuthenticateIsCalled() {
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(null);
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
        result = loginBOController.login(new AuthCredentials("someUser", "somePassword"));
    }

    private void thenVerifyAuthenticationHasBeenCalledInManager() {
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyIsEmpty() {
        assertNotNull(result);
        assertNull(result.getBody());
    }

    private void thenReceivedResponseAuthHeaderMatchesWithToken() {
        String bearerTokenRegex = "^Bearer\\s[a-zA-Z0-9-_.]+$";
        assertTrue(String.valueOf(result.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0)).matches(bearerTokenRegex));
    }
}

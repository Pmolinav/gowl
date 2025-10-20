package com.pmolinav.auth.units;

import com.pmolinav.auth.auth.TokenConfig;
import com.pmolinav.auth.controllers.LoginController;
import com.pmolinav.auth.controllers.LogoutController;
import com.pmolinav.auth.controllers.RefreshTokenController;
import com.pmolinav.auth.controllers.UserController;
import com.pmolinav.auth.services.UserService;
import com.pmolinav.auth.services.UserTokenAsyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

@RunWith(MockitoJUnitRunner.class)
class BaseUnitTest {

    @InjectMocks
    LoginController loginController;
    @InjectMocks
    RefreshTokenController refreshTokenController;
    @InjectMocks
    LogoutController logoutController;
    @InjectMocks
    UserController userController;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TokenConfig tokenConfig;
    @Mock
    UserService userServiceMock;
    @Mock
    UserTokenAsyncService userTokenAsyncServiceMock;

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}

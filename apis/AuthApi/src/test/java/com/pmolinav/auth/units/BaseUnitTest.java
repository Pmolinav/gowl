package com.pmolinav.auth.units;

import com.pmolinav.auth.auth.TokenConfig;
import com.pmolinav.auth.controllers.LoginController;
import com.pmolinav.auth.controllers.UserController;
import com.pmolinav.auth.services.UserService;
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
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TokenConfig tokenConfig;
    @Mock
    UserService userServiceMock;
    @InjectMocks
    UserController userController;

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}

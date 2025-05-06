package com.pmolinav.auth.units;

import com.pmolinav.auth.auth.TokenConfig;
import com.pmolinav.auth.controllers.LoginController;
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

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}

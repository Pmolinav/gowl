package com.pmolinav.users.units;

import com.pmolinav.users.controllers.LoginBOController;
import com.pmolinav.users.controllers.UserBOController;
import com.pmolinav.users.service.UserBOService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

@RunWith(MockitoJUnitRunner.class)
class BaseUnitTest {

    @Mock
    UserBOService userBOServiceMock;
    @InjectMocks
    UserBOController userBOController;
    @InjectMocks
    LoginBOController loginBOController;
    @Mock
    AuthenticationManager authenticationManager;

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}

package net.pmolinav.configuration.units;

import net.pmolinav.configuration.controller.ActivityBOController;
import net.pmolinav.configuration.controller.BookingBOController;
import net.pmolinav.configuration.controller.LoginBOController;
import net.pmolinav.configuration.controller.UserBOController;
import net.pmolinav.configuration.service.ActivityBOService;
import net.pmolinav.configuration.service.BookingBOService;
import net.pmolinav.configuration.service.UserBOService;
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
    @Mock
    ActivityBOService activityBOServiceMock;
    @InjectMocks
    ActivityBOController activityBOController;
    @Mock
    BookingBOService bookingBOServiceMock;
    @InjectMocks
    BookingBOController bookingBOController;
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

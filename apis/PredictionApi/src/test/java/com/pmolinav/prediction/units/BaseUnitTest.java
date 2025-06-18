package com.pmolinav.prediction.units;

import com.pmolinav.prediction.controllers.EventController;
import com.pmolinav.prediction.controllers.MatchController;
import com.pmolinav.prediction.controllers.OddsController;
import com.pmolinav.prediction.controllers.PlayerBetController;
import com.pmolinav.prediction.services.EventService;
import com.pmolinav.prediction.services.MatchService;
import com.pmolinav.prediction.services.OddsService;
import com.pmolinav.prediction.services.PlayerBetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
class BaseUnitTest {

    @Mock
    OddsService oddsServiceMock;
    @InjectMocks
    OddsController oddsController;

    @Mock
    EventService eventServiceMock;
    @InjectMocks
    EventController eventController;

    @Mock
    MatchService matchServiceMock;
    @InjectMocks
    MatchController matchController;

    @Mock
    PlayerBetService playerBetServiceMock;
    @InjectMocks
    PlayerBetController playerBetController;

    @Mock
    AuthenticationManager authenticationManager;

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        givenMockedSecurityContextWithUser("someUser");
    }

    private void givenMockedSecurityContextWithUser(String username) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(username, null, List.of());

        SecurityContext context = Mockito.mock(SecurityContext.class);

        Mockito.when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }
}

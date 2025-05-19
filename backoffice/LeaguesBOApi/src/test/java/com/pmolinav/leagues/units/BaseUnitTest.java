package com.pmolinav.leagues.units;

import com.pmolinav.leagues.controllers.LeaguesBOController;
import com.pmolinav.leagues.services.LeaguesBOService;
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
    LeaguesBOService leaguesBOServiceMock;
    @InjectMocks
    LeaguesBOController leaguesBOController;
    @Mock
    AuthenticationManager authenticationManager;

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}

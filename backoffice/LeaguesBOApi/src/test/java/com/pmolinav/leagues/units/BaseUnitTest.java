package com.pmolinav.leagues.units;

import com.pmolinav.leagues.controllers.*;
import com.pmolinav.leagues.services.*;
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
    LeagueBOController leagueBOController;

    @Mock
    LeagueCategoriesBOService leagueCategoriesBOServiceMock;
    @InjectMocks
    LeagueCategoryBOController leagueCategoriesBOController;

    @Mock
    MatchDaysBOService matchDaysBOServiceMock;
    @InjectMocks
    MatchDayBOController matchDayBOController;

    @Mock
    LeaguePlayersBOService leaguePlayersBOServiceMock;
    @InjectMocks
    LeaguePlayerBOController leaguePlayerBOController;

    @Mock
    LeaguePlayerPointsBOService leaguePlayerPointsBOServiceMock;
    @InjectMocks
    LeaguePlayerPointsBOController leaguePlayerPointsBOController;

    @Mock
    AuthenticationManager authenticationManager;

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}

package com.pmolinav.league.units;

import com.pmolinav.league.controllers.*;
import com.pmolinav.league.services.*;
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
    LeaguesService leaguesServiceMock;
    @InjectMocks
    LeagueController leagueController;

    @Mock
    LeagueCategoriesService leagueCategoriesServiceMock;
    @InjectMocks
    LeagueCategoryController leagueCategoriesController;

    @Mock
    MatchDaysService matchDaysServiceMock;
    @InjectMocks
    MatchDayController matchDayController;

    @Mock
    LeaguePlayersService leaguePlayersServiceMock;
    @InjectMocks
    LeaguePlayerController leaguePlayerController;

    @Mock
    LeaguePlayerPointsService leaguePlayerPointsServiceMock;
    @InjectMocks
    LeaguePlayerPointsController leaguePlayerPointsController;

    @Mock
    AuthenticationManager authenticationManager;

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}

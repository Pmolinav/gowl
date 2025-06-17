package com.pmolinav.prediction.units;

import com.pmolinav.prediction.controllers.*;
import com.pmolinav.prediction.services.*;
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
    PlayerBetSelectionService playerBetSelectionServiceMock;
    @InjectMocks
    PlayerBetSelectionController playerBetSelectionController;

    @Mock
    AuthenticationManager authenticationManager;

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}

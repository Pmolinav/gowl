package com.pmolinav.predictions.units;

import com.pmolinav.predictions.controllers.*;
import com.pmolinav.predictions.services.*;
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
    OddsBOService oddsBOServiceMock;
    @InjectMocks
    OddsBOController oddsBOController;

    @Mock
    EventBOService eventBOServiceMock;
    @InjectMocks
    EventBOController eventBOController;

    @Mock
    MatchBOService matchBOServiceMock;
    @InjectMocks
    MatchBOController matchBOController;

    @Mock
    PlayerBetBOService playerBetBOServiceMock;
    @InjectMocks
    PlayerBetBOController playerBetBOController;

    @Mock
    PlayerBetSelectionBOService playerBetSelectionBOServiceMock;
    @InjectMocks
    PlayerBetSelectionBOController playerBetSelectionBOController;

    @Mock
    AuthenticationManager authenticationManager;

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}

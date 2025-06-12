package com.pmolinav.predictions.unit;

import com.pmolinav.predictions.controllers.*;
import com.pmolinav.predictions.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class BaseUnitTest {
    //    @Mock
//    MessageProducer messageProducer;
    @Mock
    OddsService oddsServiceMock;
    @InjectMocks
    OddsController oddsController;
    @Mock
    MatchService matchServiceMock;
    @InjectMocks
    MatchController matchController;
    @Mock
    EventService eventServiceMock;
    @InjectMocks
    EventController eventController;
    @Mock
    PlayerBetService playerBetServiceMock;
    @InjectMocks
    PlayerBetController playerBetController;
    @Mock
    PlayerBetSelectionService playerBetSelectionServiceMock;
    @InjectMocks
    PlayerBetSelectionController playerBetSelectionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Kafka producer.
//        doNothing().when(messageProducer).sendMessage(anyString(), any(History.class));
    }

}

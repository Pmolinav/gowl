package com.pmolinav.leagues.unit;

import com.pmolinav.leagues.controllers.LeagueCategoryController;
import com.pmolinav.leagues.controllers.MatchDayController;
import com.pmolinav.leagues.services.LeagueCategoryService;
import com.pmolinav.leagues.services.MatchDayService;
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
    LeagueCategoryService leagueCategoryServiceMock;
    @InjectMocks
    LeagueCategoryController leagueCategoryController;
    @Mock
    MatchDayService matchDayServiceMock;
    @InjectMocks
    MatchDayController matchDayController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Kafka producer.
//        doNothing().when(messageProducer).sendMessage(anyString(), any(History.class));
    }

}

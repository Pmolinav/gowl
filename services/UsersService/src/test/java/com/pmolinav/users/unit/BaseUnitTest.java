package com.pmolinav.users.unit;

import com.pmolinav.users.controllers.UserController;
import com.pmolinav.users.services.UserService;
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
    UserService userServiceMock;
    @InjectMocks
    UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Kafka producer.
//        doNothing().when(messageProducer).sendMessage(anyString(), any(History.class));
    }

}

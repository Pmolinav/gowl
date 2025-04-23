package com.pmolinav.bookings.unit;

import com.pmolinav.bookings.controller.ActivityController;
import com.pmolinav.bookings.controller.BookingController;
import com.pmolinav.bookings.controller.UserController;
import com.pmolinav.bookings.producer.MessageProducer;
import com.pmolinav.bookings.service.ActivityService;
import com.pmolinav.bookings.service.BookingService;
import com.pmolinav.bookings.service.UserService;
import com.pmolinav.userslib.model.History;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
class BaseUnitTest {
    @Mock
    MessageProducer messageProducer;
    @Mock
    UserService userServiceMock;
    @InjectMocks
    UserController userController;
    @Mock
    ActivityService activityServiceMock;
    @InjectMocks
    ActivityController activityController;
    @Mock
    BookingService bookingServiceMock;
    @InjectMocks
    BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Kafka producer.
        doNothing().when(messageProducer).sendMessage(anyString(), any(History.class));
    }

}

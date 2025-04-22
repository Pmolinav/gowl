package net.pmolinav.bookings.unit;

import net.pmolinav.bookings.controller.ActivityController;
import net.pmolinav.bookings.controller.BookingController;
import net.pmolinav.bookings.controller.UserController;
import net.pmolinav.bookings.producer.MessageProducer;
import net.pmolinav.bookings.service.ActivityService;
import net.pmolinav.bookings.service.BookingService;
import net.pmolinav.bookings.service.UserService;
import net.pmolinav.bookingslib.model.History;
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

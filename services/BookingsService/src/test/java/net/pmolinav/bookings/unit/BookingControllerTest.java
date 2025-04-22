package net.pmolinav.bookings.unit;

import net.pmolinav.bookingslib.dto.BookingDTO;
import net.pmolinav.bookingslib.dto.BookingStatus;
import net.pmolinav.bookingslib.exception.InternalServerErrorException;
import net.pmolinav.bookingslib.exception.NotFoundException;
import net.pmolinav.bookingslib.model.Booking;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookingControllerTest extends BaseUnitTest {

    BookingDTO bookingDTO;
    List<Booking> expectedBookings;
    ResponseEntity<?> result;

    /* FIND ALL BOOKINGS */
    @Test
    void findAllBookingsHappyPath() {
        whenFindAllBookingsInServiceReturnedValidBookings();
        andFindAllBookingsIsCalledInController();
        thenVerifyFindAllBookingsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedBookings));
    }

    @Test
    void findAllBookingsNotFound() {
        whenFindAllBookingsInServiceThrowsNotFoundException();
        andFindAllBookingsIsCalledInController();
        thenVerifyFindAllBookingsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllBookingsServerError() {
        whenFindAllBookingsInServiceThrowsServerException();
        andFindAllBookingsIsCalledInController();
        thenVerifyFindAllBookingsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CREATE BOOKING */
    @Test
    void createBookingHappyPath() {
        givenValidBookingDTOForRequest(1L, "Pool", new Date(),
                new Date(System.currentTimeMillis() + 50000000), BookingStatus.OPEN);
        whenCreateBookingInServiceReturnedAValidBooking();
        andCreateBookingIsCalledInController();
        thenVerifyCreateBookingHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsStringIs(String.valueOf(1));
    }

    @Test
    void createBookingServerError() {
        givenValidBookingDTOForRequest(1L, "Pool", new Date(),
                new Date(System.currentTimeMillis() + 50000000), BookingStatus.OPEN);
        whenCreateBookingInServiceReturnedAValidBooking();
        whenCreateBookingInServiceThrowsServerException();
        andCreateBookingIsCalledInController();
        thenVerifyCreateBookingHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND BOOKING BY ID */
    @Test
    void findBookingByIdHappyPath() {
        whenFindBookingByIdInServiceReturnedValidBookings();
        andFindBookingByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedBookings.get(0)));
    }

    @Test
    void findBookingByIdNotFound() {
        whenFindBookingByIdInServiceThrowsNotFoundException();
        andFindBookingByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findBookingByIdServerError() {
        whenFindBookingByIdInServiceThrowsServerException();
        andFindBookingByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE BOOKING */
    @Test
    void deleteBookingHappyPath() {
        whenDeleteBookingInServiceIsOk();
        andDeleteBookingIsCalledInController();
        thenVerifyDeleteBookingHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteBookingNotFound() {
        whenDeleteBookingInServiceThrowsNotFoundException();
        andDeleteBookingIsCalledInController();
        thenVerifyDeleteBookingHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteBookingServerError() {
        whenDeleteBookingInServiceThrowsServerException();
        andDeleteBookingIsCalledInController();
        thenVerifyDeleteBookingHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void givenValidBookingDTOForRequest(long userId, String activityName, Date startTime, Date endTime, BookingStatus status) {
        bookingDTO = new BookingDTO(userId, activityName, startTime, endTime, status);
    }

    private void whenFindAllBookingsInServiceReturnedValidBookings() {
        expectedBookings = List.of(
                new Booking(1L, 22L, "Pool", new Date(), new Date(),
                        BookingStatus.OPEN.name(), new Date(), null),
                new Booking(2L, 22L, "Gym", new Date(), new Date(),
                        BookingStatus.OPEN.name(), new Date(), null));

        when(bookingServiceMock.findAllBookings()).thenReturn(expectedBookings);
    }

    private void whenFindAllBookingsInServiceThrowsNotFoundException() {
        when(bookingServiceMock.findAllBookings()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllBookingsInServiceThrowsServerException() {
        when(bookingServiceMock.findAllBookings())
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateBookingInServiceReturnedAValidBooking() {
        when(bookingServiceMock.createBooking(any())).thenReturn(new Booking(
                1L, 22L, "Pool", new Date(), new Date(),
                BookingStatus.OPEN.name(), new Date(), null));
    }

    private void whenCreateBookingInServiceThrowsServerException() {
        when(bookingServiceMock.createBooking(any(BookingDTO.class)))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindBookingByIdInServiceReturnedValidBookings() {
        expectedBookings = List.of(
                new Booking(1L, 22L, "Pool", new Date(), new Date(),
                        BookingStatus.OPEN.name(), new Date(), null));

        when(bookingServiceMock.findById(1L)).thenReturn(expectedBookings.get(0));
    }

    private void whenFindBookingByIdInServiceThrowsNotFoundException() {
        when(bookingServiceMock.findById(1L)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindBookingByIdInServiceThrowsServerException() {
        when(bookingServiceMock.findById(1L))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenDeleteBookingInServiceIsOk() {
        doNothing().when(bookingServiceMock).deleteBooking(anyLong());
    }

    private void whenDeleteBookingInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(bookingServiceMock)
                .deleteBooking(anyLong());
    }

    private void whenDeleteBookingInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(bookingServiceMock)
                .deleteBooking(anyLong());
    }

    private void andFindAllBookingsIsCalledInController() {
        result = bookingController.findAllBookings();
    }

    private void andFindBookingByIdIsCalledInController() {
        result = bookingController.findBookingById(1L);
    }

    private void andCreateBookingIsCalledInController() {
        result = bookingController.createBooking(bookingDTO);
    }

    private void andDeleteBookingIsCalledInController() {
        result = bookingController.deleteBooking(1L);
    }

    private void thenVerifyFindAllBookingsHasBeenCalledInService() {
        verify(bookingServiceMock, times(1)).findAllBookings();
    }

    private void thenVerifyCreateBookingHasBeenCalledInService() {
        verify(bookingServiceMock, times(1)).createBooking(any(BookingDTO.class));
    }

    private void thenVerifyFindByIdHasBeenCalledInService() {
        verify(bookingServiceMock, times(1)).findById(anyLong());
    }

    private void thenVerifyDeleteBookingHasBeenCalledInService() {
        verify(bookingServiceMock, times(1)).deleteBooking(anyLong());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, String.valueOf(result.getBody()));
    }
}

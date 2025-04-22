package net.pmolinav.configuration.units;

import net.pmolinav.bookingslib.dto.BookingDTO;
import net.pmolinav.bookingslib.dto.BookingStatus;
import net.pmolinav.bookingslib.exception.CustomStatusException;
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

class BookingBOControllerTest extends BaseUnitTest {

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

        when(bookingBOServiceMock.findAllBookings()).thenReturn(expectedBookings);
    }

    private void whenFindAllBookingsInServiceThrowsNotFoundException() {
        when(bookingBOServiceMock.findAllBookings()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllBookingsInServiceThrowsServerException() {
        when(bookingBOServiceMock.findAllBookings())
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenCreateBookingInServiceReturnedAValidBooking() {
        when(bookingBOServiceMock.createBooking(any())).thenReturn(1L);
    }

    private void whenCreateBookingInServiceThrowsServerException() {
        when(bookingBOServiceMock.createBooking(any(BookingDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindBookingByIdInServiceReturnedValidBookings() {
        expectedBookings = List.of(
                new Booking(1L, 22L, "Pool", new Date(), new Date(),
                        BookingStatus.OPEN.name(), new Date(), null));

        when(bookingBOServiceMock.findBookingById(1L)).thenReturn(expectedBookings.get(0));
    }

    private void whenFindBookingByIdInServiceThrowsNotFoundException() {
        when(bookingBOServiceMock.findBookingById(1L)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindBookingByIdInServiceThrowsServerException() {
        when(bookingBOServiceMock.findBookingById(1L))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenDeleteBookingInServiceIsOk() {
        doNothing().when(bookingBOServiceMock).deleteBooking(anyLong());
    }

    private void whenDeleteBookingInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(bookingBOServiceMock)
                .deleteBooking(anyLong());
    }

    private void whenDeleteBookingInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(bookingBOServiceMock)
                .deleteBooking(anyLong());
    }

    private void andFindAllBookingsIsCalledInController() {
        result = bookingBOController.findAllBookings(this.requestUid);
    }

    private void andFindBookingByIdIsCalledInController() {
        result = bookingBOController.getBookingById(this.requestUid, 1L);
    }

    private void andCreateBookingIsCalledInController() {
        result = bookingBOController.createBooking(this.requestUid, bookingDTO);
    }

    private void andDeleteBookingIsCalledInController() {
        result = bookingBOController.deleteBooking(this.requestUid, 1L);
    }

    private void thenVerifyFindAllBookingsHasBeenCalledInService() {
        verify(bookingBOServiceMock, times(1)).findAllBookings();
    }

    private void thenVerifyCreateBookingHasBeenCalledInService() {
        verify(bookingBOServiceMock, times(1)).createBooking(any(BookingDTO.class));
    }

    private void thenVerifyFindByIdHasBeenCalledInService() {
        verify(bookingBOServiceMock, times(1)).findBookingById(anyLong());
    }

    private void thenVerifyDeleteBookingHasBeenCalledInService() {
        verify(bookingBOServiceMock, times(1)).deleteBooking(anyLong());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, String.valueOf(result.getBody()));
    }
}

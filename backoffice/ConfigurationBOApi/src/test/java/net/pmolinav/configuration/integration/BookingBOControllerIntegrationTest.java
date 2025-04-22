package net.pmolinav.configuration.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pmolinav.bookingslib.dto.BookingDTO;
import net.pmolinav.bookingslib.dto.BookingStatus;
import net.pmolinav.bookingslib.model.Booking;
import net.pmolinav.configuration.client.BookingClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("net.pmolinav.bookingslib.model")
class BookingBOControllerIntegrationTest extends AbstractBaseTest {

    @MockBean
    private BookingClient bookingClient;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Booking> expectedBookings;

    @Test
    void findAllBookingsInternalServerError() throws Exception {
        andFindAllBookingsThrowsNonRetryableException();

        mockMvc.perform(get("/bookings?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAllBookingsHappyPath() throws Exception {
        andFindAllBookingsReturnedValidBookings();

        MvcResult result = mockMvc.perform(get("/bookings?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<Booking> bookingResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Booking>>() {
                });

        Assertions.assertEquals(expectedBookings, bookingResponseList);
    }

    @Test
    void createBookingBadRequest() throws Exception {
        andCreateBookingThrowsNonRetryableException();

        BookingDTO requestDto = new BookingDTO(1L, "Pool", new Date(9999999999999L),
                new Date(9999999990000L), BookingStatus.OPEN);

        mockMvc.perform(post("/bookings?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBookingServerError() throws Exception {
        andCreateBookingThrowsNonRetryableException();

        BookingDTO requestDto = new BookingDTO(1L, "Pool", new Date(9999999990000L),
                new Date(9999999999999L), BookingStatus.OPEN);

        mockMvc.perform(post("/bookings?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createBookingHappyPath() throws Exception {
        andCreateBookingReturnedValidId();

        BookingDTO requestDto = new BookingDTO(1L, "Pool", new Date(9999999990000L),
                new Date(9999999999999L), BookingStatus.OPEN);

        MvcResult result = mockMvc.perform(post("/bookings?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody, matchesPattern("\\d+"));
    }

    @Test
    void findBookingByIdServerError() throws Exception {
        andFindBookingByIdThrowsNonRetryableException();

        mockMvc.perform(get("/bookings/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findBookingByIdHappyPath() throws Exception {
        andFindBookingByIdReturnedBooking();

        MvcResult result = mockMvc.perform(get("/bookings/3?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        Booking bookingResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Booking>() {
                });

        Assertions.assertEquals(expectedBookings.get(0), bookingResponse);
    }

    @Test
    void deleteBookingByIdInternalServerError() throws Exception {
        andBookingDeleteThrowsNonRetryableException();

        mockMvc.perform(delete("/bookings/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteBookingByIdHappyPath() throws Exception {
        andBookingIsDeletedOkOnClient();

        mockMvc.perform(delete("/bookings/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    private void andBookingIsDeletedOkOnClient() {
        doNothing().when(this.bookingClient).deleteBooking(anyLong());
    }

    private void andBookingDeleteThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.bookingClient).deleteBooking(anyLong());
    }

    private void andFindBookingByIdReturnedBooking() {
        this.expectedBookings = List.of(new Booking(1L, 22L, "Pool",
                new Date(), new Date(), BookingStatus.OPEN.name(), new Date(), null));

        when(this.bookingClient.findBookingById(anyLong())).thenReturn(this.expectedBookings.get(0));
    }

    private void andFindBookingByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.bookingClient).findBookingById(anyLong());
    }

    private void andCreateBookingReturnedValidId() {
        when(this.bookingClient.createBooking(any(BookingDTO.class))).thenReturn(1L);
    }

    private void andCreateBookingThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.bookingClient).createBooking(any(BookingDTO.class));
    }

    private void andFindAllBookingsReturnedValidBookings() {
        this.expectedBookings = List.of(new Booking(1L, 22L, "Pool",
                new Date(), new Date(), BookingStatus.OPEN.name(), new Date(), null));

        when(this.bookingClient.findAllBookings()).thenReturn(this.expectedBookings);
    }

    private void andFindAllBookingsThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.bookingClient).findAllBookings();
    }
}


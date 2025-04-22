package com.pmolinav.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.pmolinav.bookingslib.dto.BookingDTO;
import com.pmolinav.bookingslib.dto.BookingStatus;
import com.pmolinav.bookingslib.model.Booking;
import com.pmolinav.database.GowlDatabaseConnector;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class BookingBOConfigurationDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8002";


    @When("^try to create a new booking with data$")
    public void tryToCreateANewBooking(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            for (Map<String, String> row : rows) {
                executePost(localURL + "/bookings",
                        objectMapper.writeValueAsString(new BookingDTO(lastUser.getUserId(),
                                lastActivity.getActivityName(),
                                row.get("start_time") != null ? new Date(Long.parseLong(row.get("start_time")))
                                        : new Date(Instant.now().toEpochMilli() + 10000),
                                row.get("end_time") != null ? new Date(Long.parseLong(row.get("end_time")))
                                        : new Date(Instant.now().toEpochMilli() + 50000),
                                BookingStatus.valueOf(row.get("status"))
                        )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get all bookings")
    public void tryToGetAllBookings() {
        executeGet(localURL + "/bookings");
    }

    @When("^try to get a booking by bookingId$")
    public void tryToGetABookingByBookingId() {
        executeGet(localURL + "/bookings/" + lastBooking.getBookingId());
    }

    @When("^try to delete a booking by bookingId$")
    public void tryToDeleteAnUserByUserId() {
        executeDelete(localURL + "/bookings/" + lastBooking.getBookingId());
    }

    @Then("a booking with status (.*) has been stored successfully$")
    public void aBookingHasBeenStored(String status) {
        try {
            dbConnector = new GowlDatabaseConnector();
            lastBooking = dbConnector.getBookingByStatus(status);
            assertNotNull(lastBooking);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("a list of bookings with statuses (.*) are returned in response$")
    public void aListOfBookingsWithStatusesIsReturned(String statuses) throws JsonProcessingException {
        List<String> statusesList = List.of(statuses.split(","));
        List<Booking> obtainedBookings = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<Booking>>() {
        });
        assertNotNull(obtainedBookings);
        for (String status : statusesList) {
            assertTrue(obtainedBookings.stream().anyMatch(booking -> booking.getStatus().equals(status)));
        }
    }

    @Then("a booking with status (.*) is returned in response$")
    public void aBookingWithStatusIsReturned(String status) throws JsonProcessingException {
        Booking obtainedBooking = objectMapper.readValue(latestResponse.getBody(), Booking.class);
        assertNotNull(obtainedBooking);
        assertEquals(status, obtainedBooking.getStatus());
    }

}
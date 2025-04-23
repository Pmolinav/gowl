package com.pmolinav.bookings.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.bookings.repository.BookingRepository;
import com.pmolinav.userslib.dto.BookingDTO;
import com.pmolinav.userslib.dto.BookingStatus;
import com.pmolinav.userslib.model.Booking;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@EnableJpaRepositories("com.pmolinav.bookings.repository")
@EntityScan("com.pmolinav.userslib.model")
class BookingsControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findAllBookingsNotFound() throws Exception {
        mockMvc.perform(get("/bookings"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllBookingsHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithIds(1, 2, true, true, true);

        MvcResult result = mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andReturn();

        List<Booking> bookingResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Booking>>() {
                });

        Assertions.assertEquals(2, bookingResponseList.size());
    }

    @Test
    void createBookingHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithIds(1, 2, true, true, false);

        BookingDTO requestDto = new BookingDTO(1L, "FOOTBALL", new Date(100),
                new Date(3000), BookingStatus.OPEN);

        MvcResult result = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody, matchesPattern("\\d+"));
    }

    @Test
    void findBookingByIdNotFound() throws Exception {
        mockMvc.perform(get("/bookings/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findBookingByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithIds(3, 4, true, true, true);

        MvcResult result = mockMvc.perform(get("/bookings/4"))
                .andExpect(status().isOk())
                .andReturn();

        Booking bookingResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Booking>() {
                });

        Assertions.assertEquals("FOOTBALL", bookingResponse.getActivityName());
        Assertions.assertEquals(3L, bookingResponse.getUserId());
    }

    @Test
    void deleteBookingByIdNotFound() throws Exception {
        mockMvc.perform(delete("/bookings/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBookingByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithIds(5, 6, true, true, true);

        mockMvc.perform(delete("/bookings/6"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/bookings/7"))
                .andExpect(status().isOk());
    }

}


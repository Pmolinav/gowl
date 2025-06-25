package com.pmolinav.predictions.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.dto.EventType;
import com.pmolinav.predictionslib.model.Event;
import com.pmolinav.predictionslib.model.Match;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.predictionslib.model")
class EventControllerIntegrationTest extends AbstractContainerBaseTest {

    @Test
    void findAllEventsNotFound() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllEventsHappyPath() throws Exception {
        givenSomePreviouslyStoredEventWithId();

        MvcResult result = mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andReturn();

        List<EventDTO> eventResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<EventDTO>>() {
                });

        assertEquals(1, eventResponseList.size());
    }

    @Test
    void findEventByEventTypeNotFound() throws Exception {
        mockMvc.perform(get("/events/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findEventByEventTypeHappyPath() throws Exception {
        Event storedEvent = givenSomePreviouslyStoredEventWithId();

        MvcResult result = mockMvc.perform(get("/events/" + storedEvent.getEventType()))
                .andExpect(status().isOk())
                .andReturn();

        EventDTO eventResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<EventDTO>() {
                });

        assertNotNull(eventResponse);
        assertEquals(storedEvent.getEventType(), eventResponse.getEventType());
    }

    @Test
    void findEventByMatchIdNotFound() throws Exception {
        mockMvc.perform(get("/events/match/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findEventByMatchIdHappyPath() throws Exception {
        Match storedMatch = givenSomePreviouslyStoredMatchWithId();
        Event storedEvent = givenSomePreviouslyStoredEventWithMatchId(storedMatch.getMatchId());

        MvcResult result = mockMvc.perform(get("/events/match/" + storedMatch.getMatchId()))
                .andExpect(status().isOk())
                .andReturn();

        List<EventDTO> eventResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<EventDTO>>() {
                });

        assertNotNull(eventResponse);
        assertEquals(1, eventResponse.size());
        assertEquals(storedEvent.getEventType(), eventResponse.getFirst().getEventType());
    }

    @Test
    void createEventHappyPath() throws Exception {
        Match match = givenSomePreviouslyStoredMatchWithId();

        EventDTO requestDto = new EventDTO();
        requestDto.setEventType(EventType.H2H.getName());
        requestDto.setMatchId(match.getMatchId());
        requestDto.setDescription("Event Description");

        MvcResult result = mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(eventRepository.existsById(responseBody));
    }

    @Test
    void deleteEventByEventTypeNotFound() throws Exception {
        mockMvc.perform(delete("/events/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEventByEventTypeHappyPath() throws Exception {
        Event event = givenSomePreviouslyStoredEventWithId();

        mockMvc.perform(delete("/events/" + event.getEventType()))
                .andExpect(status().isOk());

        assertFalse(eventRepository.existsById(event.getEventType()));
    }

}


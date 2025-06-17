package com.pmolinav.predictions.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.predictionslib.dto.EventDTO;
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
        givenSomePreviouslyStoredEventWithId();

        MvcResult result = mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andReturn();

        List<EventDTO> eventResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<EventDTO>>() {
                });

        assertEquals(2, eventResponseList.size());
    }

    @Test
    void findEventByIdNotFound() throws Exception {
        mockMvc.perform(get("/events/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findEventByIdHappyPath() throws Exception {
        Event storedEvent = givenSomePreviouslyStoredEventWithId();

        MvcResult result = mockMvc.perform(get("/events/" + storedEvent.getEventId()))
                .andExpect(status().isOk())
                .andReturn();

        EventDTO eventResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<EventDTO>() {
                });

        assertNotNull(eventResponse);
        assertEquals(storedEvent.getName(), eventResponse.getName());
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
        assertEquals(storedEvent.getName(), eventResponse.getFirst().getName());
    }

    @Test
    void createEventHappyPath() throws Exception {
        Match match = givenSomePreviouslyStoredMatchWithId();

        EventDTO requestDto = new EventDTO();
        requestDto.setMatchId(match.getMatchId());
        requestDto.setName("New Event");
        requestDto.setDescription("Event Description");

        MvcResult result = mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Long createdEventId = Long.parseLong(responseBody);

        assertTrue(eventRepository.existsById(createdEventId));
    }

    @Test
    void createEventServerError() throws Exception {
        EventDTO requestDto = new EventDTO();
        requestDto.setMatchId(999999L);
        requestDto.setName("Invalid Event");

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteEventByIdNotFound() throws Exception {
        mockMvc.perform(delete("/events/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEventByIdHappyPath() throws Exception {
        Event event = givenSomePreviouslyStoredEventWithId();

        mockMvc.perform(delete("/events/" + event.getEventId()))
                .andExpect(status().isOk());

        assertFalse(eventRepository.existsById(event.getEventId()));
    }

}


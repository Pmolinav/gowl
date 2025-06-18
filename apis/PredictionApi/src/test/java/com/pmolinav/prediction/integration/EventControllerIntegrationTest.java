package com.pmolinav.prediction.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.predictionslib.dto.EventDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.predictionslib.model")
class EventControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<EventDTO> expectedEvents;

    @Test
    void findEventByIdInternalServerError() throws Exception {
        andFindEventByIdThrowsNonRetryableException();

        mockMvc.perform(get("/events/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findEventByIdHappyPath() throws Exception {
        andFindEventByIdReturnsEvent();

        MvcResult result = mockMvc.perform(get("/events/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        EventDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), EventDTO.class);

        assertEquals(expectedEvents.getFirst(), response);
    }

    @Test
    void findEventsByMatchIdInternalServerError() throws Exception {
        andFindEventsByMatchIdThrowsNonRetryableException();

        mockMvc.perform(get("/events/match/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findEventsByMatchIdHappyPath() throws Exception {
        andFindEventsByMatchIdReturnsEvent();

        MvcResult result = mockMvc.perform(get("/events/match/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<EventDTO> responseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<EventDTO>>() {
                });

        assertEquals(expectedEvents, responseList);
    }

    private void andFindEventByIdReturnsEvent() {
        this.expectedEvents = List.of(buildEventDTO());
        when(this.eventClient.findById(anyLong())).thenReturn(expectedEvents.getFirst());
    }

    private void andFindEventByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.eventClient).findById(anyLong());
    }

    private void andFindEventsByMatchIdReturnsEvent() {
        this.expectedEvents = List.of(buildEventDTO());
        when(this.eventClient.findEventsByMatchId(anyLong())).thenReturn(expectedEvents);
    }

    private void andFindEventsByMatchIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.eventClient).findEventsByMatchId(anyLong());
    }

    private EventDTO buildEventDTO() {
        return new EventDTO(1L, 2L, "Event 1", "Event description");
    }
}
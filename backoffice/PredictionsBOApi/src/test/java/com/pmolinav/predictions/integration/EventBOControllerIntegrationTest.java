package com.pmolinav.predictions.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.dto.EventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.predictionslib.model")
class EventBOControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<EventDTO> expectedEvents;

    @Test
    void findAllEventsInternalServerError() throws Exception {
        andFindAllEventsThrowsNonRetryableException();

        mockMvc.perform(get("/events?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAllEventsHappyPath() throws Exception {
        andFindAllEventsReturnedValidEvents();

        MvcResult result = mockMvc.perform(get("/events?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<EventDTO> responseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertEquals(expectedEvents, responseList);
    }

    @Test
    void findEventByEventTypeInternalServerError() throws Exception {
        andFindEventByEventTypeThrowsNonRetryableException();

        mockMvc.perform(get("/events/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findEventByEventTypeHappyPath() throws Exception {
        andFindEventByIdReturnsEvent();

        MvcResult result = mockMvc.perform(get("/events/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        EventDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), EventDTO.class);

        assertEquals(expectedEvents.getFirst(), response);
    }

    @Test
    void createEventInternalServerError() throws Exception {
        andCreateEventThrowsNonRetryableException();

        EventDTO request = buildEventDTO();

        mockMvc.perform(post("/events?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createEventHappyPath() throws Exception {
        andCreateEventReturnsValidId();

        EventDTO request = buildEventDTO();

        MvcResult result = mockMvc.perform(post("/events?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertThat(response, matchesPattern("\\d+"));
    }

    @Test
    void deleteEventByIdInternalServerError() throws Exception {
        andDeleteEventThrowsNonRetryableException();

        mockMvc.perform(delete("/events/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteEventByIdHappyPath() throws Exception {
        andDeleteEventReturnsOk();

        mockMvc.perform(delete("/events/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    private void andFindAllEventsReturnedValidEvents() {
        this.expectedEvents = List.of(buildEventDTO());
        when(this.eventClient.findAll()).thenReturn(expectedEvents);
    }

    private void andFindAllEventsThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.eventClient).findAll();
    }

    private void andFindEventByIdReturnsEvent() {
        this.expectedEvents = List.of(buildEventDTO());
        when(this.eventClient.findByType(anyString())).thenReturn(expectedEvents.getFirst());
    }

    private void andFindEventByEventTypeThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.eventClient).findByType(anyString());
    }

    private void andCreateEventReturnsValidId() {
        when(this.eventClient.create(any(EventDTO.class))).thenReturn(1L);
    }

    private void andCreateEventThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.eventClient).create(any(EventDTO.class));
    }

    private void andDeleteEventReturnsOk() {
        doNothing().when(this.eventClient).delete(anyString());
    }

    private void andDeleteEventThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.eventClient).delete(anyString());
    }

    private EventDTO buildEventDTO() {
        return new EventDTO(EventType.H2H.getName(), EventType.H2H.getDescription());
    }
}
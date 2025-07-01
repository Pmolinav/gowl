package com.pmolinav.prediction.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.dto.EventType;
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

    private void andFindEventByIdReturnsEvent() {
        this.expectedEvents = List.of(buildEventDTO());
        when(this.eventClient.findByEventType(anyString())).thenReturn(expectedEvents.getFirst());
    }

    private void andFindEventByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.eventClient).findByEventType(anyString());
    }

    private EventDTO buildEventDTO() {
        return new EventDTO(EventType.H2H.getName(), EventType.H2H.getDescription());
    }
}
package com.pmolinav.predictions.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.predictionslib.dto.OddsDTO;
import com.pmolinav.predictionslib.model.Event;
import com.pmolinav.predictionslib.model.Odds;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.predictionslib.model")
class OddsControllerIntegrationTest extends AbstractContainerBaseTest {

    @Test
    void findAllOddsNotFound() throws Exception {
        mockMvc.perform(get("/odds"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllOddsHappyPath() throws Exception {
        givenSomePreviouslyStoredOddsWithId();

        MvcResult result = mockMvc.perform(get("/odds"))
                .andExpect(status().isOk())
                .andReturn();

        List<OddsDTO> oddsResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<OddsDTO>>() {
                });

        assertEquals(1, oddsResponseList.size());
    }

    @Test
    void findOddsByIdNotFound() throws Exception {
        mockMvc.perform(get("/odds/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findOddsByIdHappyPath() throws Exception {
        Odds odds = givenSomePreviouslyStoredOddsWithId();

        MvcResult result = mockMvc.perform(get("/odds/" + odds.getOddsId()))
                .andExpect(status().isOk())
                .andReturn();

        OddsDTO oddsResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<OddsDTO>() {
                });

        assertNotNull(oddsResponse);
        assertEquals(odds.getLabel(), oddsResponse.getLabel());
    }

    @Test
    void createOddsHappyPath() throws Exception {
        Event event = givenSomePreviouslyStoredEventWithId();

        OddsDTO requestDto = new OddsDTO();
        requestDto.setEventType(event.getEventType());
        requestDto.setLabel("Win");
        requestDto.setValue(BigDecimal.valueOf(1.75));
        requestDto.setActive(true);

        MvcResult result = mockMvc.perform(post("/odds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Long createdOddsId = Long.parseLong(responseBody);

        assertTrue(oddsRepository.existsById(createdOddsId));
    }

    @Test
    void createOddsServerError() throws Exception {
        OddsDTO requestDto = new OddsDTO();
        requestDto.setLabel("Win");
        requestDto.setValue(BigDecimal.valueOf(1.75));

        mockMvc.perform(post("/odds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteOddsByIdNotFound() throws Exception {
        mockMvc.perform(delete("/odds/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOddsByIdHappyPath() throws Exception {
        Odds odds = givenSomePreviouslyStoredOddsWithId();

        mockMvc.perform(delete("/odds/" + odds.getOddsId()))
                .andExpect(status().isOk());

        assertFalse(oddsRepository.existsById(odds.getOddsId()));
    }
}

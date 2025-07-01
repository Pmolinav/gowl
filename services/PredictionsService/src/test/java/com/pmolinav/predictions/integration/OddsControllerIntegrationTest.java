package com.pmolinav.predictions.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.predictionslib.dto.OddsDTO;
import com.pmolinav.predictionslib.model.Event;
import com.pmolinav.predictionslib.model.Match;
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
        givenSomePreviouslyStoredMatchWithId();
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
        givenSomePreviouslyStoredMatchWithId();
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
    void findEventByMatchIdNotFound() throws Exception {
        mockMvc.perform(get("/odds/match/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findEventByMatchIdHappyPath() throws Exception {
        Match storedMatch = givenSomePreviouslyStoredMatchWithId();
        Odds odds = givenSomePreviouslyStoredOddsWithId();

        MvcResult result = mockMvc.perform(get("/odds/match/" + storedMatch.getMatchId()))
                .andExpect(status().isOk())
                .andReturn();

        List<OddsDTO> oddsResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<OddsDTO>>() {
                });

        assertNotNull(oddsResponse);
        assertEquals(1, oddsResponse.size());
        assertEquals(storedMatch.getMatchId(), oddsResponse.getFirst().getMatchId());
        assertEquals(odds.getEventType(), oddsResponse.getFirst().getEventType());
    }

    @Test
    void createOddsHappyPath() throws Exception {
        Event event = givenSomePreviouslyStoredEventWithId();
        Match match = givenSomePreviouslyStoredMatchWithId();

        OddsDTO requestDto = new OddsDTO();
        requestDto.setEventType(event.getEventType());
        requestDto.setMatchId(match.getMatchId());
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
        givenSomePreviouslyStoredMatchWithId();
        Odds odds = givenSomePreviouslyStoredOddsWithId();

        mockMvc.perform(delete("/odds/" + odds.getOddsId()))
                .andExpect(status().isOk());

        assertFalse(oddsRepository.existsById(odds.getOddsId()));
    }
}

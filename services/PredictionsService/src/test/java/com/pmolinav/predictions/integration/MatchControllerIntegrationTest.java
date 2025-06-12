package com.pmolinav.predictions.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.predictionslib.dto.MatchDTO;
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
class MatchControllerIntegrationTest extends AbstractContainerBaseTest {

    @Test
    void findAllMatchesNotFound() throws Exception {
        mockMvc.perform(get("/matches"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllMatchesHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchWithId();
        givenSomePreviouslyStoredMatchWithId();

        MvcResult result = mockMvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andReturn();

        List<MatchDTO> matchResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDTO>>() {
                });

        assertEquals(2, matchResponseList.size());
    }

    @Test
    void findMatchByIdNotFound() throws Exception {
        mockMvc.perform(get("/matches/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findMatchByIdHappyPath() throws Exception {
        Match storedMatch = givenSomePreviouslyStoredMatchWithId();

        MvcResult result = mockMvc.perform(get("/matches/" + storedMatch.getMatchId()))
                .andExpect(status().isOk())
                .andReturn();

        MatchDTO matchResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<MatchDTO>() {
                });

        assertNotNull(matchResponse);
        assertEquals(storedMatch.getHomeTeam(), matchResponse.getHomeTeam());
        assertEquals(storedMatch.getAwayTeam(), matchResponse.getAwayTeam());
    }

    @Test
    void createMatchHappyPath() throws Exception {
        MatchDTO requestDto = new MatchDTO();
        requestDto.setCategoryId("PREMIER");
        requestDto.setMatchDayNumber(1);
        requestDto.setSeason(2025);
        requestDto.setHomeTeam("Team A");
        requestDto.setAwayTeam("Team B");
        requestDto.setStartTime(System.currentTimeMillis());

        MvcResult result = mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Long createdMatchId = Long.parseLong(responseBody);

        assertTrue(matchRepository.existsById(createdMatchId));
    }

    @Test
    void createMatchServerError() throws Exception {
        MatchDTO requestDto = new MatchDTO();
        requestDto.setMatchDayNumber(1);
        requestDto.setSeason(2025);
        requestDto.setHomeTeam("Team A");
        requestDto.setAwayTeam("Team B");
        requestDto.setStartTime(System.currentTimeMillis());

        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteMatchByIdNotFound() throws Exception {
        mockMvc.perform(delete("/matches/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMatchByIdHappyPath() throws Exception {
        Match match = givenSomePreviouslyStoredMatchWithId();

        mockMvc.perform(delete("/matches/" + match.getMatchId()))
                .andExpect(status().isOk());

        assertFalse(matchRepository.existsById(match.getMatchId()));
    }

}
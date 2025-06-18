package com.pmolinav.prediction.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.predictionslib.dto.MatchDTO;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.predictionslib.model")
class MatchControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<MatchDTO> expectedMatches;

    @Test
    void findMatchByIdServerError() throws Exception {
        andFindMatchByIdThrowsNonRetryableException();

        mockMvc.perform(get("/matches/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findMatchByIdHappyPath() throws Exception {
        andFindMatchByIdReturnedMatch();

        MvcResult result = mockMvc.perform(get("/matches/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        MatchDTO matchResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<MatchDTO>() {
                });

        assertEquals(expectedMatches.getFirst(), matchResponse);
    }

    @Test
    void findMatchesByCategoryIdSeasonAndMatchDayNumberServerError() throws Exception {
        andFindMatchesByCategoryIdSeasonAndMatchDayNumberThrowsNonRetryableException();

        mockMvc.perform(get("/matches/categories/PREMIER/seasons/2025/number/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findMatchesByCategoryIdSeasonAndMatchDayNumberHappyPath() throws Exception {
        andFindMatchesByCategoryIdSeasonAndMatchDayNumberReturnedMatch();

        MvcResult result = mockMvc.perform(get("/matches/categories/PREMIER/seasons/2025/number/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<MatchDTO> matchesResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDTO>>() {
                });

        assertEquals(expectedMatches, matchesResponse);
    }

    private void andFindMatchByIdReturnedMatch() {
        expectedMatches = List.of(new MatchDTO(1L, "PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, "ACTIVE"));

        when(this.matchClient.findById(anyLong())).thenReturn(expectedMatches.getFirst());
    }

    private void andFindMatchByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.matchClient).findById(anyLong());
    }

    private void andFindMatchesByCategoryIdSeasonAndMatchDayNumberReturnedMatch() {
        expectedMatches = List.of(new MatchDTO(1L, "PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, "ACTIVE"));

        when(this.matchClient.findByCategoryIdSeasonAndMatchDayNumber(anyString(), anyInt(), anyInt()))
                .thenReturn(expectedMatches);
    }

    private void andFindMatchesByCategoryIdSeasonAndMatchDayNumberThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.matchClient).findByCategoryIdSeasonAndMatchDayNumber(anyString(), anyInt(), anyInt());
    }

}


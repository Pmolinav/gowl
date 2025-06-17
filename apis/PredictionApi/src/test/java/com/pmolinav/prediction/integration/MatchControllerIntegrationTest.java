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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void findAllMatchesInternalServerError() throws Exception {
        andFindAllMatchesThrowsNonRetryableException();

        mockMvc.perform(get("/matches?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAllMatchesHappyPath() throws Exception {
        andFindAllMatchesReturnedValidMatches();

        MvcResult result = mockMvc.perform(get("/matches?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<MatchDTO> matchResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDTO>>() {
                });

        assertEquals(expectedMatches, matchResponseList);
    }

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
    void createMatchServerError() throws Exception {
        andCreateMatchThrowsNonRetryableException();

        MatchDTO matchDTO = new MatchDTO(1L, "PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, "ACTIVE");

        mockMvc.perform(post("/matches?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matchDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createMatchHappyPath() throws Exception {
        andCreateMatchReturnedValidId();

        MatchDTO matchDTO = new MatchDTO(1L, "PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, "ACTIVE");

        MvcResult result = mockMvc.perform(post("/matches?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matchDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody, matchesPattern("\\d+"));
    }

    @Test
    void deleteMatchByIdInternalServerError() throws Exception {
        andDeleteMatchThrowsNonRetryableException();

        mockMvc.perform(delete("/matches/321?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteMatchByIdHappyPath() throws Exception {
        andMatchDeletedOk();

        mockMvc.perform(delete("/matches/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    private void andFindAllMatchesReturnedValidMatches() {
        expectedMatches = List.of(new MatchDTO(1L, "PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, "ACTIVE"));

        when(this.matchClient.findAll()).thenReturn(expectedMatches);
    }

    private void andFindAllMatchesThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.matchClient).findAll();
    }

    private void andFindMatchByIdReturnedMatch() {
        expectedMatches = List.of(new MatchDTO(1L, "PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, "ACTIVE"));

        when(this.matchClient.findById(anyLong())).thenReturn(expectedMatches.getFirst());
    }

    private void andFindMatchByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.matchClient).findById(anyLong());
    }

    private void andCreateMatchReturnedValidId() {
        when(this.matchClient.create(any(MatchDTO.class))).thenReturn(1L);
    }

    private void andCreateMatchThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.matchClient).create(any(MatchDTO.class));
    }

    private void andMatchDeletedOk() {
        doNothing().when(this.matchClient).delete(anyLong());
    }

    private void andDeleteMatchThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.matchClient).delete(anyLong());
    }
}


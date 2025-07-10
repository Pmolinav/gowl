package com.pmolinav.prediction.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.predictionslib.dto.EventType;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
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
class PlayerBetControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<PlayerBetDTO> expectedPlayerBets;

    @Test
    void findPlayerBetByIdInternalServerError() throws Exception {
        andFindPlayerBetByIdThrowsNonRetryableException();

        mockMvc.perform(get("/player-bets/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findPlayerBetByIdHappyPath() throws Exception {
        andFindPlayerBetByIdReturnedPlayerBet();

        MvcResult result = mockMvc.perform(get("/player-bets/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        PlayerBetDTO response = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<PlayerBetDTO>() {
                });

        assertEquals(expectedPlayerBets.getFirst(), response);
    }

    @Test
    void findPlayerBetsByMatchIdInternalServerError() throws Exception {
        andFindPlayerBetsByMatchIdThrowsNonRetryableException();

        mockMvc.perform(get("/player-bets/match/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findPlayerBestByMatchIdHappyPath() throws Exception {
        andFindPlayerBetsByMatchIdReturnedPlayerBets();

        MvcResult result = mockMvc.perform(get("/player-bets/match/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<PlayerBetDTO> responseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<PlayerBetDTO>>() {
                });

        assertEquals(expectedPlayerBets, responseList);
    }

    @Test
    void findPlayerBetsByUsernameInternalServerError() throws Exception {
        andFindPlayerBetsByUsernameThrowsNonRetryableException();

        mockMvc.perform(get("/player-bets/username/someUser?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findPlayerBestByUsernameHappyPath() throws Exception {
        andFindPlayerBetsByUsernameReturnedPlayerBets();

        MvcResult result = mockMvc.perform(get("/player-bets/username/someUser?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<PlayerBetDTO> responseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<PlayerBetDTO>>() {
                });

        assertEquals(expectedPlayerBets, responseList);
    }

    @Test
    void createPlayerBetInternalServerError() throws Exception {
        andCreatePlayerBetThrowsNonRetryableException();

        PlayerBetDTO requestDto = new PlayerBetDTO("someUser", 2L, 1L, BigDecimal.TEN,
                List.of(new PlayerBetSelectionDTO(EventType.H2H.getName(), 3L, BigDecimal.ONE)));

        mockMvc.perform(post("/player-bets?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createPlayerBetHappyPath() throws Exception {
        andCreatePlayerBetReturnedValidId();

        PlayerBetDTO requestDto = new PlayerBetDTO("someUser", 2L, 1L, BigDecimal.TEN,
                List.of(new PlayerBetSelectionDTO(EventType.H2H.getName(), 3L, BigDecimal.ONE)));

        MvcResult result = mockMvc.perform(post("/player-bets?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody, matchesPattern("\\d+"));
    }

    @Test
    void deletePlayerBetByIdInternalServerError() throws Exception {
        andFindPlayerBetByIdReturnedPlayerBet();
        andDeletePlayerBetThrowsNonRetryableException();

        mockMvc.perform(delete("/player-bets/99?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deletePlayerBetByIdHappyPath() throws Exception {
        andFindPlayerBetByIdReturnedPlayerBet();
        andDeletePlayerBetReturnsOk();

        mockMvc.perform(delete("/player-bets/7?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    // ----- Mock setup helpers -----

    private void andFindPlayerBetByIdReturnedPlayerBet() {
        this.expectedPlayerBets = List.of(
                new PlayerBetDTO("someUser", 2L, 1L, BigDecimal.TEN, null)
        );
        when(this.predictionsServiceClient.findPlayerBetById(anyLong())).thenReturn(expectedPlayerBets.getFirst());
    }

    private void andFindPlayerBetByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.predictionsServiceClient).findPlayerBetById(anyLong());
    }

    private void andFindPlayerBetsByMatchIdReturnedPlayerBets() {
        this.expectedPlayerBets = List.of(
                new PlayerBetDTO("someUser", 2L, 1L, BigDecimal.TEN, null)
        );
        when(this.predictionsServiceClient.findPlayerBetsByMatchId(anyLong())).thenReturn(expectedPlayerBets);
    }

    private void andFindPlayerBetsByMatchIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.predictionsServiceClient).findPlayerBetsByMatchId(anyLong());
    }

    private void andFindPlayerBetsByUsernameReturnedPlayerBets() {
        this.expectedPlayerBets = List.of(
                new PlayerBetDTO("someUser", 2L, 1L, BigDecimal.TEN, null)
        );
        when(this.predictionsServiceClient.findPlayerBetsByUsername(anyString())).thenReturn(expectedPlayerBets);
    }

    private void andFindPlayerBetsByUsernameThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.predictionsServiceClient).findPlayerBetsByUsername(anyString());
    }

    private void andCreatePlayerBetReturnedValidId() {
        when(this.predictionsServiceClient.create(any(PlayerBetDTO.class))).thenReturn(10L);
    }

    private void andCreatePlayerBetThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.predictionsServiceClient).create(any(PlayerBetDTO.class));
    }

    private void andDeletePlayerBetReturnsOk() {
        doNothing().when(this.predictionsServiceClient).delete(anyLong());
    }

    private void andDeletePlayerBetThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.predictionsServiceClient).delete(anyLong());
    }
}

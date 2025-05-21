package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.leagueslib.model")
class LeaguePlayerPointsBOControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<LeaguePlayerPointsDTO> expectedLeaguePlayerPoints;

    @Test
    void findLeaguePlayerPointsByLeagueIdAndPlayerInternalServerError() throws Exception {
        andFindLeaguePlayerPointsByLeagueIdAndPlayerThrowsNonRetryableException();

        mockMvc.perform(get("/league-players-points/leagues/2/players/aPlayer?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeaguePlayerPointsByLeagueIdAndPlayerHappyPath() throws Exception {
        andFindLeaguePlayerPointsByLeagueIdAndPlayerReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/league-players-points/leagues/15/players/someUser?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<LeaguePlayerPointsDTO> leaguePlayerPointsResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeaguePlayerPointsDTO>>() {
                });

        assertEquals(expectedLeaguePlayerPoints, leaguePlayerPointsResponseList);
    }

    @Test
    void findLeaguePlayerPointsByCategorySeasonAndNumberInternalServerError() throws Exception {
        andFindLeaguePlayerPointsByCategorySeasonAndNumberThrowsNonRetryableException();

        mockMvc.perform(get("/league-players-points/categories/fakeCategory" +
                        "/seasons/2021/number/4?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeaguePlayerPointsByCategorySeasonAndNumberHappyPath() throws Exception {
        andFindLeaguePlayerPointsByCategorySeasonAndNumberReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/league-players-points/categories/PREMIER" +
                        "/seasons/2025/number/4?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<LeaguePlayerPointsDTO> leaguePlayerPointsResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeaguePlayerPointsDTO>>() {
                });

        assertEquals(expectedLeaguePlayerPoints, leaguePlayerPointsResponseList);
    }

    @Test
    void createLeaguePlayerPointsServerError() throws Exception {
        andCreateLeaguePlayerPointsThrowsNonRetryableException();

        LeaguePlayerPointsDTO requestDto = new LeaguePlayerPointsDTO("PREMIER", 2025,
                10, 1L, "someUser", 42);

        mockMvc.perform(post("/league-players-points?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createLeaguePlayerPointsHappyPath() throws Exception {
        andCreateLeaguePlayerPointsReturnedValidId();

        LeaguePlayerPointsDTO requestDto = new LeaguePlayerPointsDTO("PREMIER", 2025,
                10, 1L, "someUser", 42);

        MvcResult result = mockMvc.perform(post("/league-players-points?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals(this.expectedLeaguePlayerPoints.getFirst(), objectMapper.readValue(responseBody, new TypeReference<LeaguePlayerPointsDTO>() {
        }));
    }

    @Test
    void deleteLeaguePlayerPointsByLeagueIdAndPlayerInternalServerError() throws Exception {
        andLeaguePlayerPointsDeleteByLeagueIdAndPlayerThrowsNonRetryableException();

        mockMvc.perform(delete("/league-players-points/leagues/33/players/otherUser?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteLeaguePlayerPointsByLeagueIdAndPlayerHappyPath() throws Exception {
        andLeaguePlayerPointsAreDeletedOkOnClient();

        mockMvc.perform(delete("/league-players-points/leagues/45/players/someUser?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLeaguePlayerPointsByCategoryIdSeasonAndNumberInternalServerError() throws Exception {
        andLeaguePlayerPointsDeleteByCategoryIdSeasonAndNumberThrowsNonRetryableException();

        mockMvc.perform(delete("/league-players-points/categories/fake/seasons/2020/number/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteLeaguePlayerPointsByCategoryIdSeasonAndNumberHappyPath() throws Exception {
        andLeaguePlayerPointsAreDeletedOkOnClient();

        mockMvc.perform(delete("/league-players-points/categories/PREMIER/seasons/2025/number/3?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    private void andLeaguePlayerPointsAreDeletedOkOnClient() {
        doNothing().when(this.leaguePlayerPointsClient).deleteLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString());
        doNothing().when(this.leaguePlayerPointsClient).deleteLeaguePlayerByCategorySeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void andLeaguePlayerPointsDeleteByLeagueIdAndPlayerThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguePlayerPointsClient).deleteLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void andLeaguePlayerPointsDeleteByCategoryIdSeasonAndNumberThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguePlayerPointsClient).deleteLeaguePlayerByCategorySeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void andCreateLeaguePlayerPointsReturnedValidId() {
        this.expectedLeaguePlayerPoints = List.of(
                new LeaguePlayerPointsDTO("PREMIER", 2025,
                        10, 1L, "someUser", 42)
        );

        when(this.leaguePlayerPointsClient.createLeaguePlayersPoints(any(LeaguePlayerPointsDTO.class)))
                .thenReturn(this.expectedLeaguePlayerPoints.getFirst());
    }

    private void andCreateLeaguePlayerPointsThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguePlayerPointsClient).createLeaguePlayersPoints(any(LeaguePlayerPointsDTO.class));
    }

    private void andFindLeaguePlayerPointsByLeagueIdAndPlayerReturnedValidLeagues() {
        this.expectedLeaguePlayerPoints = List.of(
                new LeaguePlayerPointsDTO("PREMIER", 2025,
                        10, 1L, "someUser", 42)
        );

        when(this.leaguePlayerPointsClient.findLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString()))
                .thenReturn(this.expectedLeaguePlayerPoints);
    }

    private void andFindLeaguePlayerPointsByCategorySeasonAndNumberReturnedValidLeagues() {
        this.expectedLeaguePlayerPoints = List.of(
                new LeaguePlayerPointsDTO("PREMIER", 2025,
                        10, 1L, "someUser", 42)
        );

        when(this.leaguePlayerPointsClient.findLeaguePlayerPointsByCategorySeasonAndNumber(anyString(), anyInt(), anyInt()))
                .thenReturn(this.expectedLeaguePlayerPoints);
    }

    private void andFindLeaguePlayerPointsByLeagueIdAndPlayerThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguePlayerPointsClient).findLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void andFindLeaguePlayerPointsByCategorySeasonAndNumberThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguePlayerPointsClient).findLeaguePlayerPointsByCategorySeasonAndNumber(anyString(), anyInt(), anyInt());
    }
}


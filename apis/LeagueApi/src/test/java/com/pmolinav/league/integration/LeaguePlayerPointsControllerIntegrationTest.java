package com.pmolinav.league.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import com.pmolinav.leagueslib.model.LeagueStatus;
import com.pmolinav.leagueslib.model.PlayerStatus;
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
@EntityScan("com.pmolinav.leagueslib.model")
class LeaguePlayerPointsControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<LeaguePlayerPointsDTO> expectedLeaguePlayerPoints;

    @Test
    void findLeaguePlayerPointsByLeagueIdAndPlayerInternalServerError() throws Exception {
        andFindLeagueByIdReturnedLeague(2);
        andFindLeaguePlayerPointsByLeagueIdAndPlayerThrowsNonRetryableException();

        mockMvc.perform(get("/league-player-points/leagues/2/players/aPlayer?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeaguePlayerPointsByLeagueIdAndPlayerHappyPath() throws Exception {
        andFindLeagueByIdReturnedLeague(15);
        andFindLeaguePlayerPointsByLeagueIdAndPlayerReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/league-player-points/leagues/15/players/someUser?requestUid=" + requestUid)
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

        mockMvc.perform(get("/league-player-points/categories/fakeCategory" +
                        "/seasons/2021/number/4?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeaguePlayerPointsByCategorySeasonAndNumberHappyPath() throws Exception {
        andFindLeaguePlayerPointsByCategorySeasonAndNumberReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/league-player-points/categories/PREMIER" +
                        "/seasons/2025/number/4?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<LeaguePlayerPointsDTO> leaguePlayerPointsResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeaguePlayerPointsDTO>>() {
                });

        assertEquals(expectedLeaguePlayerPoints, leaguePlayerPointsResponseList);
    }

    private void andFindLeaguePlayerPointsByLeagueIdAndPlayerReturnedValidLeagues() {
        this.expectedLeaguePlayerPoints = List.of(
                new LeaguePlayerPointsDTO("PREMIER", 2025,
                        10, 1L, "someUser", 42)
        );

        when(this.leaguesServiceClient.findLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString()))
                .thenReturn(this.expectedLeaguePlayerPoints);
    }

    private void andFindLeaguePlayerPointsByCategorySeasonAndNumberReturnedValidLeagues() {
        this.expectedLeaguePlayerPoints = List.of(
                new LeaguePlayerPointsDTO("PREMIER", 2025,
                        10, 1L, "someUser", 42)
        );

        when(this.leaguesServiceClient.findLeaguePlayerPointsByCategorySeasonAndNumber(anyString(), anyInt(), anyInt()))
                .thenReturn(this.expectedLeaguePlayerPoints);
    }

    private void andFindLeaguePlayerPointsByLeagueIdAndPlayerThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).findLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void andFindLeaguePlayerPointsByCategorySeasonAndNumberThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).findLeaguePlayerPointsByCategorySeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void andFindLeagueByIdReturnedLeague(long leagueId) {
        LeagueDTO expectedLeague = new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                22, null, false, "someUser",
                List.of(new LeaguePlayerDTO(leagueId, "someUser", 30, PlayerStatus.ACTIVE)));

        when(this.leaguesServiceClient.findLeagueById(anyLong())).thenReturn(expectedLeague);
    }
}


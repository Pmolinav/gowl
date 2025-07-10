package com.pmolinav.league.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import com.pmolinav.leagueslib.model.LeagueStatus;
import com.pmolinav.leagueslib.model.PlayerStatus;
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
class LeaguePlayerControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<LeaguePlayerDTO> expectedLeaguePlayers;
    private List<LeagueDTO> expectedLeagues;
    private List<LeaguePlayerId> expectedLeaguePlayerIds;

    @Test
    void findLeaguePlayerByLeagueIdAndPlayerInternalServerError() throws Exception {
        andFindLeagueByIdReturnedLeague(33);
        andFindLeaguePlayerByLeagueIdAndPlayerThrowsNonRetryableException();

        mockMvc.perform(get("/league-players/leagues/33/players/fakeUser?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeaguePlayerByLeagueIdAndPlayerHappyPath() throws Exception {
        andFindLeagueByIdReturnedLeague(22);
        andFindLeaguePlayerByLeagueIdAndPlayerReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/league-players/leagues/22/players/someUser?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        LeaguePlayerDTO leaguePlayerResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LeaguePlayerDTO>() {
                });

        assertEquals(expectedLeaguePlayers.getFirst(), leaguePlayerResponseList);
    }

    @Test
    void findLeaguePlayersByLeagueIdInternalServerError() throws Exception {
        andFindLeagueByIdReturnedLeague(433);
        andFindLeaguePlayersByLeagueIdThrowsNonRetryableException();

        mockMvc.perform(get("/league-players/leagues/433?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeaguePlayersByLeagueIdHappyPath() throws Exception {
        andFindLeagueByIdReturnedLeague(343);
        andFindLeaguePlayersByLeagueIdReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/league-players/leagues/343?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<LeaguePlayerDTO> leaguePlayerResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeaguePlayerDTO>>() {
                });

        assertEquals(expectedLeaguePlayers, leaguePlayerResponseList);
    }

    @Test
    void findLeaguesByPlayerInternalServerError() throws Exception {
        andFindLeagueByIdReturnedLeague(33);
        andFindLeaguesByUsernameThrowsNonRetryableException();

        mockMvc.perform(get("/league-players/players/someUser/leagues?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeaguesByPlayerHappyPath() throws Exception {
        andFindLeagueByIdReturnedLeague(33);
        andFindLeaguesByUsernameReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/league-players/players/someUser/leagues?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<LeagueDTO> leagueResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeagueDTO>>() {
                });

        assertEquals(expectedLeagues, leagueResponseList);
    }

    @Test
    void createLeaguePlayersServerError() throws Exception {
        andFindLeagueByIdReturnedLeague(343);
        andCreateLeaguePlayersThrowsNonRetryableException();

        List<LeaguePlayerDTO> requestDto = List.of(
                new LeaguePlayerDTO(343L, "someUser", 10, PlayerStatus.ACTIVE)
        );

        mockMvc.perform(post("/league-players?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createLeaguePlayersHappyPath() throws Exception {
        andFindLeagueByIdReturnedLeague(343);
        andCreateLeaguePlayersReturnedValidIds();

        List<LeaguePlayerDTO> requestDto = List.of(
                new LeaguePlayerDTO(343L, "someUser", 10, PlayerStatus.ACTIVE)
        );

        MvcResult result = mockMvc.perform(post("/league-players?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals(this.expectedLeaguePlayerIds, objectMapper.readValue(responseBody, new TypeReference<List<LeaguePlayerId>>() {
        }));
    }

    @Test
    void deleteLeaguePlayersByLeagueIdAndPlayerInternalServerError() throws Exception {
        andFindLeagueByIdReturnedLeague(333);
        andLeaguePlayerDeleteByLeagueIdAndPlayerThrowsNonRetryableException();

        mockMvc.perform(delete("/league-players/leagues/333/players/someUser?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteLeaguePlayersByCategoryIdAndSeasonHappyPath() throws Exception {
        andFindLeagueByIdReturnedLeague(532);
        andLeaguePlayersAreDeletedOkOnClient();

        mockMvc.perform(delete("/league-players/leagues/532/players/someUser?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }


    private void andLeaguePlayersAreDeletedOkOnClient() {
        doNothing().when(this.leaguesServiceClient).deleteLeaguePlayersByLeagueId(anyLong());
        doNothing().when(this.leaguesServiceClient).deleteLeaguePlayersByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void andLeaguePlayerDeleteByLeagueIdAndPlayerThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).deleteLeaguePlayersByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void andCreateLeaguePlayersReturnedValidIds() {
        this.expectedLeaguePlayerIds = List.of(
                new LeaguePlayerId(343L, "someUser"),
                new LeaguePlayerId(343L, "otherUser")
        );

        when(this.leaguesServiceClient.createLeaguePlayers(anyList()))
                .thenReturn(this.expectedLeaguePlayerIds);
    }


    private void andCreateLeaguePlayersThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).createLeaguePlayers(anyList());
    }

    private void andFindLeaguePlayerByLeagueIdAndPlayerReturnedValidLeagues() {
        this.expectedLeaguePlayers = List.of(
                new LeaguePlayerDTO(1L, "someUser", 10, PlayerStatus.ACTIVE)
        );

        when(this.leaguesServiceClient.findLeaguePlayerByLeagueIdAndPlayer(anyLong(), anyString()))
                .thenReturn(this.expectedLeaguePlayers.getFirst());
    }

    private void andFindLeaguePlayersByLeagueIdReturnedValidLeagues() {
        this.expectedLeaguePlayers = List.of(
                new LeaguePlayerDTO(1L, "someUser", 10, PlayerStatus.ACTIVE)
        );

        when(this.leaguesServiceClient.findLeaguePlayersByLeagueId(anyLong()))
                .thenReturn(this.expectedLeaguePlayers);
    }

    private void andFindLeaguesByUsernameReturnedValidLeagues() {
        this.expectedLeagues = List.of(
                new LeagueDTO("New League", "League description",
                        "PREMIER", true, null, LeagueStatus.ACTIVE, 10,
                        null, false, "someUser", null)
        );

        when(this.leaguesServiceClient.findLeaguesByUsername(anyString()))
                .thenReturn(this.expectedLeagues);
    }

    private void andFindLeaguePlayersByLeagueIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).findLeaguePlayersByLeagueId(anyLong());
    }

    private void andFindLeaguePlayerByLeagueIdAndPlayerThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).findLeaguePlayerByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void andFindLeaguesByUsernameThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).findLeaguesByUsername(anyString());
    }

    private void andFindLeagueByIdReturnedLeague(long leagueId) {
        LeagueDTO expectedLeague = new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                22, null, false, "someUser",
                List.of(new LeaguePlayerDTO(leagueId, "someUser", 30, PlayerStatus.ACTIVE)));

        when(this.leaguesServiceClient.findLeagueById(anyLong())).thenReturn(expectedLeague);
    }
}


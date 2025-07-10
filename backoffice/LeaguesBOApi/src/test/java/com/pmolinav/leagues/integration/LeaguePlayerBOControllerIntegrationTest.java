package com.pmolinav.leagues.integration;

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
class LeaguePlayerBOControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<LeaguePlayerDTO> expectedLeaguePlayers;
    private List<LeagueDTO> expectedLeagues;
    private List<LeaguePlayerId> expectedLeaguePlayerIds;

    @Test
    void findLeaguePlayerByLeagueIdAndPlayerInternalServerError() throws Exception {
        andFindLeaguePlayerByLeagueIdAndPlayerThrowsNonRetryableException();

        mockMvc.perform(get("/league-players/leagues/33/players/fakeUser?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeaguePlayerByLeagueIdAndPlayerHappyPath() throws Exception {
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
        andFindLeaguePlayersByLeagueIdThrowsNonRetryableException();

        mockMvc.perform(get("/league-players/leagues/433?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeaguePlayersByLeagueIdHappyPath() throws Exception {
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
        andFindLeaguesByUsernameThrowsNonRetryableException();

        mockMvc.perform(get("/league-players/players/aPlayer/leagues?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeaguesByPlayerHappyPath() throws Exception {
        andFindLeaguesByUsernameReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/league-players/players/somePlayer/leagues?requestUid=" + requestUid)
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
        andCreateLeaguePlayersThrowsNonRetryableException();

        List<LeaguePlayerDTO> requestDto = List.of(
                new LeaguePlayerDTO(343L, "someUser", 10, PlayerStatus.ACTIVE),
                new LeaguePlayerDTO(343L, "otherUser", 44, PlayerStatus.ACTIVE)
        );

        mockMvc.perform(post("/league-players?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createLeaguePlayersHappyPath() throws Exception {
        andCreateLeaguePlayersReturnedValidIds();

        List<LeaguePlayerDTO> requestDto = List.of(
                new LeaguePlayerDTO(343L, "someUser", 10, PlayerStatus.ACTIVE),
                new LeaguePlayerDTO(343L, "otherUser", 44, PlayerStatus.ACTIVE)
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
    void addPointsToLeaguePlayerServerError() throws Exception {
        andAddPointsToLeaguePlayerThrowsNonRetryableException();

        List<LeaguePlayerDTO> requestDto = List.of(
                new LeaguePlayerDTO(1L, "someUser", 10, PlayerStatus.ACTIVE)
        );

        mockMvc.perform(put("/league-players/leagues/4232/players/fakeUser?points=3&requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addPointsToLeaguePlayerHappyPath() throws Exception {
        andAddPointsToLeaguePlayerOK();

        List<LeaguePlayerDTO> requestDto = List.of(
                new LeaguePlayerDTO(1L, "someUser", 10, PlayerStatus.ACTIVE),
                new LeaguePlayerDTO(1L, "otherUser", 10, PlayerStatus.ACTIVE)
        );

        mockMvc.perform(put("/league-players/leagues/442/players/someUser?points=3&requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLeaguePlayersByCategoryIdInternalServerError() throws Exception {
        andLeaguePlayerDeleteByLeagueIdThrowsNonRetryableException();

        mockMvc.perform(delete("/league-players/leagues/12?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteLeaguePlayersByCategoryIddHappyPath() throws Exception {
        andLeaguePlayersAreDeletedOkOnClient();

        mockMvc.perform(delete("/league-players/leagues/442?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLeaguePlayersByLeagueIdAndPlayerInternalServerError() throws Exception {
        andLeaguePlayerDeleteByLeagueIdAndPlayerThrowsNonRetryableException();

        mockMvc.perform(delete("/league-players/leagues/333/players/fakePlayer?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteLeaguePlayersByCategoryIdAndSeasonHappyPath() throws Exception {
        andLeaguePlayersAreDeletedOkOnClient();

        mockMvc.perform(delete("/league-players/leagues/532/players/somePlayer?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }


    private void andLeaguePlayersAreDeletedOkOnClient() {
        doNothing().when(this.leaguesServiceClient).deleteLeaguePlayersByLeagueId(anyLong());
        doNothing().when(this.leaguesServiceClient).deleteLeaguePlayersByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void andLeaguePlayerDeleteByLeagueIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).deleteLeaguePlayersByLeagueId(anyLong());
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

    private void andAddPointsToLeaguePlayerOK() {
        this.expectedLeaguePlayerIds = List.of(
                new LeaguePlayerId(343L, "someUser"),
                new LeaguePlayerId(343L, "otherUser")
        );

        doNothing().when(this.leaguesServiceClient).addPointsToLeaguePlayer(anyLong(), anyString(), anyInt());
    }


    private void andAddPointsToLeaguePlayerThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).addPointsToLeaguePlayer(anyLong(), anyString(), anyInt());
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
}


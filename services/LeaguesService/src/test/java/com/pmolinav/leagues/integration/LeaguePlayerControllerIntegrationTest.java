package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.PlayerStatus;
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
@EntityScan("com.pmolinav.leagueslib.model")
class LeaguePlayerControllerIntegrationTest extends AbstractContainerBaseTest {

    @Test
    void findLeaguePlayersByLeagueIdNotFound() throws Exception {
        mockMvc.perform(get("/league-players/leagues/56"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findLeaguePlayersByLeagueIdHappyPath() throws Exception {
        givenSomePreviouslyStoredLeaguePlayerWithId("someUser");

        MvcResult result = mockMvc.perform(get("/league-players/leagues/" + leagueId))
                .andExpect(status().isOk())
                .andReturn();

        List<LeaguePlayerDTO> leaguePlayerResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeaguePlayerDTO>>() {
                });

        assertEquals(1, leaguePlayerResponseList.size());
    }

    @Test
    void findLeaguePlayersByLeagueIdAndUsernameNotFound() throws Exception {
        mockMvc.perform(get("/league-players/leagues/56/players/fakeUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findLeaguePlayersByLeagueIdAndUsernameHappyPath() throws Exception {
        givenSomePreviouslyStoredLeaguePlayerWithId("someUser");

        MvcResult result = mockMvc.perform(get("/league-players/leagues/" + leagueId + "/players/someUser"))
                .andExpect(status().isOk())
                .andReturn();

        LeaguePlayerDTO leaguePlayerResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LeaguePlayerDTO>() {
                });

        assertNotNull(leaguePlayerResponseList);
    }

    @Test
    void findLeaguesByUsernameNotFound() throws Exception {
        mockMvc.perform(get("/league-players/players/56/leagues"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findLeaguesByUsernameHappyPath() throws Exception {
        givenSomePreviouslyStoredLeaguePlayerWithId("someUser");

        MvcResult result = mockMvc.perform(get("/league-players/players/someUser/leagues"))
                .andExpect(status().isOk())
                .andReturn();

        List<LeagueDTO> leagueResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeagueDTO>>() {
                });

        assertEquals(1, leagueResponseList.size());
    }

    @Test
    void createLeaguePlayerHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueCategoryWithId("PERMIER");
        givenSomePreviouslyStoredLeagueWithId("PREMIER", "someUser");
        List<LeaguePlayerDTO> requestDto = List.of(
                new LeaguePlayerDTO(leagueId, "someUser", 22, PlayerStatus.ACTIVE),
                new LeaguePlayerDTO(leagueId, "otherUser", 66, PlayerStatus.ACTIVE)
        );

        MvcResult result = mockMvc.perform(post("/league-players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals("[{\"leagueId\":" + leagueId + ",\"username\":\"someUser\"},{\"leagueId\":" + leagueId + ",\"username\":\"otherUser\"}]", responseBody);

        assertFalse(leaguePlayerRepository.findByLeagueId(leagueId).isEmpty());
    }

    @Test
    void deleteLeaguePlayersByLeagueIdNotFound() throws Exception {
        mockMvc.perform(delete("/league-players/leagues/88"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLeaguePlayersByLeagueIdHappyPath() throws Exception {
        givenSomePreviouslyStoredLeaguePlayerWithId("someUser");

        mockMvc.perform(delete("/league-players/leagues/" + leagueId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLeaguePlayerByLeagueIdAndUsernameNotFound() throws Exception {
        mockMvc.perform(delete("/league-players/leagues/54/players/fakePlayer"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLeaguePlayerByLeagueIdAndUsernameHappyPath() throws Exception {
        givenSomePreviouslyStoredLeaguePlayerWithId("someUser");

        mockMvc.perform(delete("/league-players/leagues/" + leagueId + "/players/someUser"))
                .andExpect(status().isOk());
    }

}


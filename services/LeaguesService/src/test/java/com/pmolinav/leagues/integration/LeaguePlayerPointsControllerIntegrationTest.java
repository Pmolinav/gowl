package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.leagueslib.model")
class LeaguePlayerPointsControllerIntegrationTest extends AbstractContainerBaseTest {

    @Test
    void findLeaguePlayerPointsByLeagueIdAndUsernameNotFound() throws Exception {
        mockMvc.perform(get("/league-player-points/leagues/333/players/fakeUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findLeaguePlayerPointsByLeagueIdAndUsernameHappyPath() throws Exception {
        givenSomePreviouslyStoredLeaguePlayerPointsWithId("someUser", 2025, 1);

        MvcResult result = mockMvc.perform(get("/league-player-points/leagues/"
                        + leagueId + "/players/someUser"))
                .andExpect(status().isOk())
                .andReturn();

        List<LeaguePlayerPointsDTO> leaguePlayerPointsResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeaguePlayerPointsDTO>>() {
                });

        assertEquals(1, leaguePlayerPointsResponseList.size());
    }

    @Test
    void findLeaguePlayerPointsByCategoryIdSeasonAndNumberNotFound() throws Exception {
        mockMvc.perform(get("/league-player-points/categories/invented/seasons/2044/number/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findLeaguePlayerPointsByCategoryIdSeasonAndNumberHappyPath() throws Exception {
        givenSomePreviouslyStoredLeaguePlayerPointsWithId("someUser", 2025, 1);

        MvcResult result = mockMvc.perform(get("/league-player-points/categories/"
                        + "someCategory" + "/seasons/2025/number/1"))
                .andExpect(status().isOk())
                .andReturn();

        List<LeaguePlayerPointsDTO> leaguePlayerPointsResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeaguePlayerPointsDTO>>() {
                });

        assertEquals(1, leaguePlayerPointsResponseList.size());
    }

    @Test
    void createLeaguePlayerPointsHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("PREMIER", 2023, 10);
        givenSomePreviouslyStoredLeaguePlayerWithId("someUser");

        LeaguePlayerPointsDTO requestDto = new LeaguePlayerPointsDTO("PREMIER",
                2023, 10, leagueId, "someUser", 36);

        MvcResult result = mockMvc.perform(post("/league-player-points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals("{\"categoryId\":\"PREMIER\",\"season\":2023,\"matchDayNumber\":10,\"leagueId\":" + leagueId + ",\"username\":\"someUser\",\"points\":36}", responseBody);

        assertFalse(leaguePlayerPointsRepository.findByLeagueId(leagueId).isEmpty());
    }

    @Test
    void deleteLeaguePlayerByLeagueIdAndUsernameNotFound() throws Exception {
        mockMvc.perform(delete("/league-player-points/leagues/54/players/fakePlayer"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLeaguePlayerByLeagueIdAndUsernameHappyPath() throws Exception {
        givenSomePreviouslyStoredLeaguePlayerPointsWithId("someUser", 2024, 8);

        mockMvc.perform(delete("/league-player-points/leagues/" + leagueId + "/players/someUser"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLeaguePlayerByCategoryIdSeasonAndNumberNotFound() throws Exception {
        mockMvc.perform(delete("/league-player-points/categories/invented/seasons/2010/number/9"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLeaguePlayerByCategoryIdSeasonAndNumberHappyPath() throws Exception {
        givenSomePreviouslyStoredLeaguePlayerPointsWithId("someUser", 2024, 8);

        mockMvc.perform(delete("/league-player-points/categories/"
                        + "someCategory" + "/seasons/2024/number/8"))
                .andExpect(status().isOk());
    }

}


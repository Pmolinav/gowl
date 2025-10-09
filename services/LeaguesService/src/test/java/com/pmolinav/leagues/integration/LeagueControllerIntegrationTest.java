package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.League;
import com.pmolinav.leagueslib.model.LeagueStatus;
import com.pmolinav.leagueslib.model.PlayerStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.leagueslib.model")
class LeagueControllerIntegrationTest extends AbstractContainerBaseTest {

    @Test
    void findAllLeaguesNotFound() throws Exception {
        mockMvc.perform(get("/leagues"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllLeaguesHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueWithId("PREMIER", "someUser");
        givenSomePreviouslyStoredLeagueWithId("PREMIER2", "someUser");

        MvcResult result = mockMvc.perform(get("/leagues"))
                .andExpect(status().isOk())
                .andReturn();

        List<LeagueDTO> leagueResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeagueDTO>>() {
                });

        assertEquals(2, leagueResponseList.size());
    }

    @Test
    void findLeagueByIdNotFound() throws Exception {
        mockMvc.perform(get("/leagues/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findLeagueByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueWithId("PREMIER", "someUser");

        MvcResult result = mockMvc.perform(get("/leagues/" + leagueId))
                .andExpect(status().isOk())
                .andReturn();

        LeagueDTO leagueResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LeagueDTO>() {
                });

        assertNotNull(leagueResponseList);
    }

    @Test
    void findLeagueByNameNotFound() throws Exception {
        mockMvc.perform(get("/leagues/names/fakeLeagueName"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findLeagueByNameHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueWithId("PREMIER2", "someUser");

        MvcResult result = mockMvc.perform(get("/leagues/names/Some League PREMIER2"))
                .andExpect(status().isOk())
                .andReturn();

        LeagueDTO leagueResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LeagueDTO>() {
                });

        assertNotNull(leagueResponse);
    }

    @Test
    void findLeaguesByUsernameNotFound() throws Exception {
        mockMvc.perform(get("/leagues/username/fakeUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findLeaguesByUsernameHappyPath() throws Exception {
        givenSomePreviouslyStoredLeaguePlayerWithId("someUser");

        MvcResult result = mockMvc.perform(get("/leagues/username/someUser"))
                .andExpect(status().isOk())
                .andReturn();

        List<LeagueDTO> leagueResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeagueDTO>>() {
                });

        assertNotNull(leagueResponseList);
        assertEquals(1, leagueResponseList.size());
    }

    @Test
    void createLeagueHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueCategoryWithId("PREMIER");

        LeagueDTO requestDto = new LeagueDTO("New League", "League description",
                "PREMIER", true, null, LeagueStatus.ACTIVE, 10,
                null, false, "someUser",
                List.of(new LeaguePlayerDTO("someUser", 33, PlayerStatus.ACTIVE)));

        MvcResult result = mockMvc.perform(post("/leagues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals(leagueId + 1, Long.parseLong(responseBody));

        assertTrue(leagueRepository.existsById(Long.parseLong(responseBody)));
        assertFalse(leaguePlayerRepository.findByLeagueId(Long.parseLong(responseBody)).isEmpty());
    }

    @Test
    void closeLeagueByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueWithId("PREMIER2", "someUser");

        mockMvc.perform(put("/leagues/close/" + leagueId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<League> league = leagueRepository.findById(leagueId);

        assertTrue(league.isPresent());
        assertEquals(LeagueStatus.CLOSED, leagueRepository.findById(leagueId).get().getStatus());
    }

    @Test
    void closeLeagueByNameHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueWithId("PREMIER2", "someUser");

        mockMvc.perform(put("/leagues/close/names/" + "Some League " + "PREMIER2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<League> league = leagueRepository.findById(leagueId);

        assertTrue(league.isPresent());
        assertEquals(LeagueStatus.CLOSED, leagueRepository.findByName("Some League " + "PREMIER2").get().getStatus());
    }

    @Test
    void deleteLeagueByIdNotFound() throws Exception {
        mockMvc.perform(delete("/leagues/88"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLeagueByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueWithId("PREMIER2", "someUser");

        mockMvc.perform(delete("/leagues/" + leagueId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLeagueByNameNotFound() throws Exception {
        mockMvc.perform(delete("/leagues/names/fake league name"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLeagueByNameHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueWithId("PREMIER", "someUser");

        mockMvc.perform(delete("/leagues/names/Some League PREMIER"))
                .andExpect(status().isOk());
    }

}


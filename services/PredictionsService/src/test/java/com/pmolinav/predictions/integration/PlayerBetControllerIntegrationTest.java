package com.pmolinav.predictions.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
import com.pmolinav.predictionslib.model.Odds;
import com.pmolinav.predictionslib.model.PlayerBet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.predictionslib.model")
class PlayerBetControllerIntegrationTest extends AbstractContainerBaseTest {

    @Test
    void findAllPlayerBetsNotFound() throws Exception {
        mockMvc.perform(get("/player-bets"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllPlayerBetsHappyPath() throws Exception {
        givenSomePreviouslyStoredPlayerBetWithId("user1");

        MvcResult result = mockMvc.perform(get("/player-bets"))
                .andExpect(status().isOk())
                .andReturn();

        List<PlayerBetDTO> playerBetList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<PlayerBetDTO>>() {
                });

        assertEquals(1, playerBetList.size());
    }

    @Test
    void findPlayerBetByIdNotFound() throws Exception {
        mockMvc.perform(get("/player-bets/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findPlayerBetByIdHappyPath() throws Exception {
        PlayerBet playerBet = givenSomePreviouslyStoredPlayerBetWithId("user1");

        MvcResult result = mockMvc.perform(get("/player-bets/" + playerBet.getBetId()))
                .andExpect(status().isOk())
                .andReturn();

        PlayerBetDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<PlayerBetDTO>() {
                });

        assertNotNull(dto);
        assertEquals("user1", dto.getUsername());
    }

    @Test
    void createPlayerBetHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchWithId();
        Odds odds = givenSomePreviouslyStoredOddsWithId();

        PlayerBetDTO request = new PlayerBetDTO("newUser",
                lastMatch.getMatchId(), 1L, BigDecimal.TEN, null);
        request.setSelections(List.of(
                new PlayerBetSelectionDTO(lastEvent.getEventType(), odds.getOddsId(), BigDecimal.ONE)));

        MvcResult result = mockMvc.perform(post("/player-bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Long createdId = Long.parseLong(responseBody);

        assertTrue(playerBetRepository.existsById(createdId));
    }

    @Test
    void createPlayerBetWithInvalidData() throws Exception {
        PlayerBetDTO request = new PlayerBetDTO(); // Missing fields

        mockMvc.perform(post("/player-bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePlayerBetByIdNotFound() throws Exception {
        mockMvc.perform(delete("/player-bets/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePlayerBetByIdHappyPath() throws Exception {
        PlayerBet playerBet = givenSomePreviouslyStoredPlayerBetWithId("user1");

        mockMvc.perform(delete("/player-bets/" + playerBet.getBetId()))
                .andExpect(status().isOk());

        assertFalse(playerBetRepository.existsById(playerBet.getBetId()));
    }
}
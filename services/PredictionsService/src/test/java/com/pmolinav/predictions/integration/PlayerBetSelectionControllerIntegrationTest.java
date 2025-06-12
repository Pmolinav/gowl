package com.pmolinav.predictions.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
import com.pmolinav.predictionslib.model.Odds;
import com.pmolinav.predictionslib.model.PlayerBet;
import com.pmolinav.predictionslib.model.PlayerBetSelection;
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
class PlayerBetSelectionControllerIntegrationTest extends AbstractContainerBaseTest {

    @Test
    void findAllPlayerBetSelectionsNotFound() throws Exception {
        mockMvc.perform(get("/player-bet-selections"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllPlayerBetSelectionsHappyPath() throws Exception {
        givenSomePreviouslyStoredPlayerBetSelectionWithId("someUser");
        givenSomePreviouslyStoredPlayerBetSelectionWithId("someUser");

        MvcResult result = mockMvc.perform(get("/player-bet-selections"))
                .andExpect(status().isOk())
                .andReturn();

        List<PlayerBetSelectionDTO> selectionList = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<PlayerBetSelectionDTO>>() {
                }
        );

        assertEquals(2, selectionList.size());
    }

    @Test
    void findPlayerBetSelectionByIdNotFound() throws Exception {
        mockMvc.perform(get("/player-bet-selections/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findPlayerBetSelectionByIdHappyPath() throws Exception {
        PlayerBetSelection selection = givenSomePreviouslyStoredPlayerBetSelectionWithId("someUser");

        MvcResult result = mockMvc.perform(get("/player-bet-selections/" + selection.getSelectionId()))
                .andExpect(status().isOk())
                .andReturn();

        PlayerBetSelectionDTO dto = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<PlayerBetSelectionDTO>() {
                });

        assertNotNull(dto);
        assertEquals(selection.getSelectionId(), dto.getSelectionId());
    }

    @Test
    void createPlayerBetSelectionHappyPath() throws Exception {
        Odds odds = givenSomePreviouslyStoredOddsWithId();
        PlayerBet playerBet = givenSomePreviouslyStoredPlayerBetWithId("someUser");

        PlayerBetSelectionDTO request = new PlayerBetSelectionDTO();
        request.setBetId(playerBet.getBetId());
        request.setOddsId(odds.getOddsId());
        request.setStake(BigDecimal.valueOf(20.0));

        MvcResult result = mockMvc.perform(post("/player-bet-selections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Long createdId = Long.parseLong(responseBody);

        assertTrue(playerBetSelectionRepository.existsById(createdId));
    }

    @Test
    void createPlayerBetSelectionInvalidData() throws Exception {
        PlayerBetSelectionDTO invalidRequest = new PlayerBetSelectionDTO(); // Missing data

        mockMvc.perform(post("/player-bet-selections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deletePlayerBetSelectionByIdNotFound() throws Exception {
        mockMvc.perform(delete("/player-bet-selections/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePlayerBetSelectionByIdHappyPath() throws Exception {
        PlayerBetSelection selection = givenSomePreviouslyStoredPlayerBetSelectionWithId("someUser");

        mockMvc.perform(delete("/player-bet-selections/" + selection.getSelectionId()))
                .andExpect(status().isOk());

        assertFalse(playerBetSelectionRepository.existsById(selection.getSelectionId()));
    }
}
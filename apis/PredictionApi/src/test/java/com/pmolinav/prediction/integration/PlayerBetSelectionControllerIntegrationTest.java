package com.pmolinav.prediction.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class PlayerBetSelectionControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<PlayerBetSelectionDTO> expectedSelections;

    @Test
    void findAllPlayerBetSelectionsInternalServerError() throws Exception {
        andFindAllSelectionsThrowsException();

        mockMvc.perform(get("/player-bet-selections?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAllPlayerBetSelectionsHappyPath() throws Exception {
        andFindAllSelectionsReturnsValidList();

        MvcResult result = mockMvc.perform(get("/player-bet-selections?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<PlayerBetSelectionDTO> responseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<PlayerBetSelectionDTO>>() {
                });

        assertEquals(expectedSelections, responseList);
    }

    @Test
    void findPlayerBetSelectionByIdInternalServerError() throws Exception {
        andFindByIdThrowsException();

        mockMvc.perform(get("/player-bet-selections/99?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findPlayerBetSelectionByIdHappyPath() throws Exception {
        andFindByIdReturnsValid();

        MvcResult result = mockMvc.perform(get("/player-bet-selections/4?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        PlayerBetSelectionDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), PlayerBetSelectionDTO.class);

        assertEquals(expectedSelections.getFirst(), response);
    }

    @Test
    void createPlayerBetSelectionInternalServerError() throws Exception {
        andCreateSelectionThrowsException();

        PlayerBetSelectionDTO dto = new PlayerBetSelectionDTO(1L, 2L, 3L, BigDecimal.ONE);

        mockMvc.perform(post("/player-bet-selections?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createPlayerBetSelectionHappyPath() throws Exception {
        andCreateSelectionReturnsValidId();

        PlayerBetSelectionDTO dto = new PlayerBetSelectionDTO(1L, 2L, 3L, BigDecimal.ONE);

        MvcResult result = mockMvc.perform(post("/player-bet-selections?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody, matchesPattern("\\d+"));
    }

    @Test
    void deletePlayerBetSelectionInternalServerError() throws Exception {
        andDeleteSelectionThrowsException();

        mockMvc.perform(delete("/player-bet-selections/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deletePlayerBetSelectionHappyPath() throws Exception {
        andDeleteSelectionReturnsOk();

        mockMvc.perform(delete("/player-bet-selections/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    // Stubs

    private void andFindAllSelectionsThrowsException() {
        doThrow(new RuntimeException("error")).when(this.playerBetSelectionClient).findAll();
    }

    private void andFindAllSelectionsReturnsValidList() {
        expectedSelections = List.of(new PlayerBetSelectionDTO(1L, 2L, 3L, BigDecimal.ONE),
                new PlayerBetSelectionDTO(1L, 2L, 3L, BigDecimal.ONE));
        when(this.playerBetSelectionClient.findAll()).thenReturn(expectedSelections);
    }

    private void andFindByIdThrowsException() {
        doThrow(new RuntimeException("error")).when(this.playerBetSelectionClient).findById(anyLong());
    }

    private void andFindByIdReturnsValid() {
        expectedSelections = List.of(new PlayerBetSelectionDTO(1L, 2L, 3L, BigDecimal.ONE));
        when(this.playerBetSelectionClient.findById(4L)).thenReturn(expectedSelections.getFirst());
    }

    private void andCreateSelectionThrowsException() {
        doThrow(new RuntimeException("error")).when(this.playerBetSelectionClient).create(any(PlayerBetSelectionDTO.class));
    }

    private void andCreateSelectionReturnsValidId() {
        when(this.playerBetSelectionClient.create(any(PlayerBetSelectionDTO.class))).thenReturn(101L);
    }

    private void andDeleteSelectionThrowsException() {
        doThrow(new RuntimeException("error")).when(this.playerBetSelectionClient).delete(anyLong());
    }

    private void andDeleteSelectionReturnsOk() {
        doNothing().when(this.playerBetSelectionClient).delete(anyLong());
    }
}

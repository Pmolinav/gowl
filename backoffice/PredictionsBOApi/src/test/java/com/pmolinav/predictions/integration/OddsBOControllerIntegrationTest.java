package com.pmolinav.predictions.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.predictionslib.dto.OddsDTO;
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
class OddsBOControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<OddsDTO> expectedOdds;

    @Test
    void findAllOddsInternalServerError() throws Exception {
        andFindAllOddsThrowsNonRetryableException();

        mockMvc.perform(get("/odds?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAllOddsHappyPath() throws Exception {
        andFindAllOddsReturnedValidOdds();

        MvcResult result = mockMvc.perform(get("/odds?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<OddsDTO> responseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<OddsDTO>>() {
                });

        assertEquals(expectedOdds, responseList);
    }

    @Test
    void createOddsServerError() throws Exception {
        andCreateOddsThrowsNonRetryableException();

        OddsDTO requestDto = new OddsDTO(1L, "LABEL1", BigDecimal.valueOf(2.0), true);

        mockMvc.perform(post("/odds?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createOddsHappyPath() throws Exception {
        andCreateOddsReturnedValidId();

        OddsDTO requestDto = new OddsDTO(1L, "LABEL1", BigDecimal.valueOf(2.0), true);

        MvcResult result = mockMvc.perform(post("/odds?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody, matchesPattern("\\d+"));
    }

    @Test
    void findOddsByIdInternalServerError() throws Exception {
        andFindOddsByIdThrowsNonRetryableException();

        mockMvc.perform(get("/odds/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findOddsByIdHappyPath() throws Exception {
        andFindOddsByIdReturnedOdds();

        MvcResult result = mockMvc.perform(get("/odds/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        OddsDTO responseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<OddsDTO>() {
                });

        assertEquals(expectedOdds.getFirst(), responseList);
    }

    @Test
    void deleteOddsByIdInternalServerError() throws Exception {
        andDeleteOddsByIdThrowsNonRetryableException();

        mockMvc.perform(delete("/odds/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteOddsByIdHappyPath() throws Exception {
        andDeleteOddsByIdReturnsOk();

        mockMvc.perform(delete("/odds/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    // ----- Mock setup helpers -----

    private void andFindAllOddsReturnedValidOdds() {
        this.expectedOdds = List.of(new OddsDTO(1L, "LABEL1", BigDecimal.valueOf(2.0), true));
        when(this.oddsClient.findAll()).thenReturn(expectedOdds);
    }

    private void andFindAllOddsThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.oddsClient).findAll();
    }

    private void andCreateOddsReturnedValidId() {
        when(this.oddsClient.create(any(OddsDTO.class))).thenReturn(10L);
    }

    private void andCreateOddsThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.oddsClient).create(any(OddsDTO.class));
    }

    private void andFindOddsByIdReturnedOdds() {
        this.expectedOdds = List.of(new OddsDTO(1L, "LABEL1", BigDecimal.valueOf(2.0), true));
        when(this.oddsClient.findById(anyLong())).thenReturn(expectedOdds.getFirst());
    }

    private void andFindOddsByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.oddsClient).findById(anyLong());
    }

    private void andDeleteOddsByIdReturnsOk() {
        doNothing().when(this.oddsClient).delete(anyLong());
    }

    private void andDeleteOddsByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.oddsClient).delete(anyLong());
    }
}

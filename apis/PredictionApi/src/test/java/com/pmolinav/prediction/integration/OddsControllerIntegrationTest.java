package com.pmolinav.prediction.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.predictionslib.dto.OddsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.predictionslib.model")
class OddsControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<OddsDTO> expectedOdds;

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

        OddsDTO response = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<OddsDTO>() {
                });

        assertEquals(expectedOdds.getFirst(), response);
    }

    @Test
    void findOddsByEventIdInternalServerError() throws Exception {
        andFindOddsByEventIdThrowsNonRetryableException();

        mockMvc.perform(get("/odds/event/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findOddsByEventIdHappyPath() throws Exception {
        andFindOddsByEventIdReturnedOdds();

        MvcResult result = mockMvc.perform(get("/odds/event/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<OddsDTO> responseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<OddsDTO>>() {
                });

        assertEquals(expectedOdds, responseList);
    }

    // ----- Mock setup helpers -----

    private void andFindOddsByIdReturnedOdds() {
        this.expectedOdds = List.of(new OddsDTO(1L, 1L, "LABEL1", BigDecimal.valueOf(2.0), true));
        when(this.oddsClient.findById(anyLong())).thenReturn(expectedOdds.getFirst());
    }

    private void andFindOddsByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.oddsClient).findById(anyLong());
    }

    private void andFindOddsByEventIdReturnedOdds() {
        this.expectedOdds = List.of(new OddsDTO(1L, 1L, "LABEL1", BigDecimal.valueOf(2.0), true));
        when(this.oddsClient.findByEventId(anyLong())).thenReturn(expectedOdds);
    }

    private void andFindOddsByEventIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.oddsClient).findByEventId(anyLong());
    }

}

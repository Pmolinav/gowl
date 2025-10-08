package com.pmolinav.league.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.dto.SimpleMatchDayDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.leagueslib.model")
class MatchDayControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<MatchDayDTO> expectedMatchDays;
    private List<SimpleMatchDayDTO> expectedSimpleMatchDays;

    @Test
    void findMatchDaysByCategoryIdAndSeasonInternalServerError() throws Exception {
        andFindMatchDaysByCategoryIdAndSeasonThrowsNonRetryableException();

        mockMvc.perform(get("/match-days/categories/fakeCategory/seasons/2022?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findMatchDaysByCategoryIdAndSeasonHappyPath() throws Exception {
        andFindMatchDaysByCategoryIdAndSeasonReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/match-days/categories/PREMIER/seasons/2025?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<SimpleMatchDayDTO> matchDayResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<SimpleMatchDayDTO>>() {
                });

        assertEquals(expectedSimpleMatchDays, matchDayResponseList);
    }

    private void andFindMatchDaysByCategoryIdAndSeasonReturnedValidLeagues() {
        this.expectedMatchDays = List.of(new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L));
        try {
            this.expectedSimpleMatchDays = objectMapper.readValue("[{\"label\":\"J 10\", \"value\": \"10\"}]",
                    new TypeReference<List<SimpleMatchDayDTO>>() {
                    });
        } catch (JsonProcessingException e) {
            fail();
        }
        when(this.matchDaysClient.findMatchDayByCategoryIdAndSeason(anyString(), anyInt()))
                .thenReturn(this.expectedMatchDays);
    }

    private void andFindMatchDaysByCategoryIdAndSeasonThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.matchDaysClient).findMatchDayByCategoryIdAndSeason(anyString(), anyInt());
    }
}


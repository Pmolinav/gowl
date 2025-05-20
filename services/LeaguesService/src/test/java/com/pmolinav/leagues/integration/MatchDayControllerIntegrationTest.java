package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.model.MatchDayId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.leagueslib.model")
class MatchDayControllerIntegrationTest extends AbstractContainerBaseTest {

    @Test
    void findAllMatchDaysNotFound() throws Exception {
        mockMvc.perform(get("/match-days"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllMatchDaysHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 26);

        MvcResult result = mockMvc.perform(get("/match-days"))
                .andExpect(status().isOk())
                .andReturn();

        List<MatchDayDTO> matchDayResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDayDTO>>() {
                });

        assertEquals(1, matchDayResponseList.size());
    }

    @Test
    void createMatchDayHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueCategoryWithId("PREMIER");
        MatchDayDTO requestDto = new MatchDayDTO("PREMIER", 2025, 26, 123L, 1234L);

        MvcResult result = mockMvc.perform(post("/match-days")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals("{\"categoryId\":\"PREMIER\",\"season\":2025,\"matchDayNumber\":26}", responseBody);

        assertTrue(matchDayRepository.existsById(objectMapper.readValue(responseBody, MatchDayId.class)));
    }

    @Test
    void createMatchDaysHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueCategoryWithId("PREMIER2");
        List<MatchDayDTO> requestListDto = List.of(
                new MatchDayDTO("PREMIER2", 2025, 1, 123L, 1234L),
                new MatchDayDTO("PREMIER2", 2025, 2, 1234L, 1235L),
                new MatchDayDTO("PREMIER2", 2025, 3, 1235L, 12356L)
        );

        MvcResult result = mockMvc.perform(post("/match-days/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestListDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("PREMIER2"));

        assertEquals(3L, matchDayRepository.findByCategoryId("PREMIER2").size());
    }

    @Test
    void findMatchDayByByCategoryIdNotFound() throws Exception {
        mockMvc.perform(get("/match-days/categories/OTHER_CATEGORY"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findMatchDaysByCategoryIdHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 26);
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 27);

        MvcResult result = mockMvc.perform(get("/match-days/categories/SOME_CATEGORY"))
                .andExpect(status().isOk())
                .andReturn();

        List<MatchDayDTO> matchDaysResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDayDTO>>() {
                });

        assertTrue(matchDaysResponse.stream().allMatch(matchDay -> matchDay.getCategoryId().equals("SOME_CATEGORY")));
    }

    @Test
    void findMatchDayByByCategoryIdAndSeasonNotFound() throws Exception {
        mockMvc.perform(get("/match-days/categories/OTHER_CATEGORY/seasons/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findMatchDaysByCategoryIdAndSeasonHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 26);
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2024, 27);

        MvcResult result = mockMvc.perform(get("/match-days/categories/SOME_CATEGORY/seasons/2025"))
                .andExpect(status().isOk())
                .andReturn();

        List<MatchDayDTO> matchDaysResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDayDTO>>() {
                });

        assertEquals(1, matchDaysResponse.size());
        assertTrue(matchDaysResponse.stream().allMatch(matchDay ->
                matchDay.getCategoryId().equals("SOME_CATEGORY") && matchDay.getSeason() == 2025));
    }

    @Test
    void deleteMatchDaysByCategoryIdNotFound() throws Exception {
        mockMvc.perform(delete("/match-days/categories/OTHER_CATEGORY"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMatchDaysByCategoryIdHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 11);
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 12);

        mockMvc.perform(delete("/match-days/categories/SOME_CATEGORY"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMatchDaysByCategoryIdAndSeasonNotFound() throws Exception {
        mockMvc.perform(delete("/match-days/categories/OTHER_CATEGORY/seasons/2023"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMatchDaysByCategoryIdAndSeasonHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 11);
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 12);

        mockMvc.perform(delete("/match-days/categories/SOME_CATEGORY/seasons/2025"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberNotFound() throws Exception {
        mockMvc.perform(delete("/match-days/categories/OTHER_CATEGORY/seasons/2023/number/33"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 11);

        mockMvc.perform(delete("/match-days/categories/SOME_CATEGORY/seasons/2025/number/11"))
                .andExpect(status().isOk());
    }

}


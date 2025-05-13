package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagues.repositories.MatchDayRepository;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@EntityScan("com.pmolinav.leagueslib.model")
class MatchDayControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MatchDayRepository matchDayRepository;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findAllMatchDaysNotFound() throws Exception {
        mockMvc.perform(get("/matchdays"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllMatchDaysHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 26);

        MvcResult result = mockMvc.perform(get("/matchdays"))
                .andExpect(status().isOk())
                .andReturn();

        List<MatchDayDTO> matchDayResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDayDTO>>() {
                });

        assertEquals(1, matchDayResponseList.size());
    }

    @Test
    void createMatchDayHappyPath() throws Exception {
        MatchDayDTO requestDto = new MatchDayDTO("PREMIER", 2025, 26, 123L, 1234L);

        MvcResult result = mockMvc.perform(post("/matchdays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals("{\"categoryId\":\"PREMIER\",\"season\":2025,\"matchDayNumber\":26}", responseBody);
    }

    @Test
    void findMatchDayByByCategoryIdNotFound() throws Exception {
        mockMvc.perform(get("/matchdays/categories/OTHER_CATEGORY"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findMatchDaysByCategoryIdHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 26);
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 27);

        MvcResult result = mockMvc.perform(get("/matchdays/categories/SOME_CATEGORY"))
                .andExpect(status().isOk())
                .andReturn();

        List<MatchDayDTO> matchDaysResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDayDTO>>() {
                });

        assertTrue(matchDaysResponse.stream().allMatch(matchDay -> matchDay.getCategoryId().equals("SOME_CATEGORY")));
    }

    @Test
    void findMatchDayByByCategoryIdAndSeasonNotFound() throws Exception {
        mockMvc.perform(get("/matchdays/categories/OTHER_CATEGORY/seasons/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findMatchDaysByCategoryIdAndSeasonHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 26);
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2024, 27);

        MvcResult result = mockMvc.perform(get("/matchdays/categories/SOME_CATEGORY/seasons/2025"))
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
    void deleteMatchDayByCategoryIdNotFound() throws Exception {
        mockMvc.perform(delete("/matchdays/categories/OTHER_CATEGORY"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMatchDayByCategoryIdHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 11);
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 12);

        mockMvc.perform(delete("/matchdays/categories/SOME_CATEGORY"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMatchDayByCategoryIdAndSeasonNotFound() throws Exception {
        mockMvc.perform(delete("/matchdays/categories/OTHER_CATEGORY/seasons/2023"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMatchDayByCategoryIdAndSeasonHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 11);
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 12);

        mockMvc.perform(delete("/matchdays/categories/SOME_CATEGORY/seasons/2025"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMatchDayByCategoryIdSeasonAndNumberNotFound() throws Exception {
        mockMvc.perform(delete("/matchdays/categories/OTHER_CATEGORY/seasons/2023/number/33"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMatchDayByCategoryIdSeasonAndNumberHappyPath() throws Exception {
        givenSomePreviouslyStoredMatchDayWithId("SOME_CATEGORY", 2025, 11);

        mockMvc.perform(delete("/matchdays/categories/SOME_CATEGORY/seasons/2025/number/11"))
                .andExpect(status().isOk());
    }

}


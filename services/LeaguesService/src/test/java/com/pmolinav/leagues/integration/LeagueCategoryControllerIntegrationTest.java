package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.model.LeagueCategory;
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
class LeagueCategoryControllerIntegrationTest extends AbstractContainerBaseTest {

    @Test
    void findAllLeagueCategoriesNotFound() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllLeagueCategoriesHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueCategoryWithId("SOME_CATEGORY");

        MvcResult result = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        List<LeagueCategory> leagueCategoryResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeagueCategory>>() {
                });

        assertEquals(1, leagueCategoryResponseList.size());
    }

    @Test
    void createLeagueCategoryHappyPath() throws Exception {
        LeagueCategoryDTO requestDto = new LeagueCategoryDTO("PREMIER", "Premier League",
                "English Premier League", "FOOTBALL", "UK", null, true);


        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals("PREMIER", responseBody);

        assertTrue(leagueCategoryRepository.existsById(responseBody));
    }

    @Test
    void findLeagueCategoryByIdNotFound() throws Exception {
        mockMvc.perform(get("/categories/OTHER_CATEGORY"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findLeagueCategoryByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueCategoryWithId("SOME_CATEGORY");

        MvcResult result = mockMvc.perform(get("/categories/SOME_CATEGORY"))
                .andExpect(status().isOk())
                .andReturn();

        LeagueCategory leagueCategoryResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LeagueCategory>() {
                });

        assertEquals("SOME_CATEGORY", leagueCategoryResponse.getCategoryId());
    }

    @Test
    void deleteLeagueCategoryByIdNotFound() throws Exception {
        mockMvc.perform(delete("/categories/OTHER_CATEGORY"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLeagueCategoryByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredLeagueCategoryWithId("SOME_CATEGORY");

        mockMvc.perform(delete("/categories/SOME_CATEGORY"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/categories/SOME_CATEGORY"))
                .andExpect(status().isNotFound());
    }

}


package com.pmolinav.league.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagueslib.model.LeagueCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.leagueslib.model")
class LeagueCategoryControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<LeagueCategory> expectedLeagueCategories;

    @Test
    void findAllLeagueCategoriesInternalServerError() throws Exception {
        andFindAllLeagueCategoriesThrowsNonRetryableException();

        mockMvc.perform(get("/categories?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAllLeagueCategoriesHappyPath() throws Exception {
        andFindAllLeagueCategoriesReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/categories?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<LeagueCategory> leagueCategoryResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeagueCategory>>() {
                });

        assertEquals(expectedLeagueCategories, leagueCategoryResponseList);
    }

    @Test
    void findLeagueCategoryByIdServerError() throws Exception {
        andFindLeagueCategoryByIdThrowsNonRetryableException();

        mockMvc.perform(get("/categories/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeagueCategoryByIdHappyPath() throws Exception {
        andFindLeagueCategoryByIdReturnedLeague();

        MvcResult result = mockMvc.perform(get("/categories/3?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        LeagueCategory leagueCategoryResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LeagueCategory>() {
                });

        assertEquals(expectedLeagueCategories.getFirst(), leagueCategoryResponse);
    }

    private void andFindLeagueCategoryByIdReturnedLeague() {
        this.expectedLeagueCategories = List.of(new LeagueCategory("CALCIO", "Calcio Serie A",
                "Italian League", "FOOTBALL", "IT", "localhost",
                true, 123L, null));

        when(this.leagueCategoriesClient.findLeagueCategoryById(anyString()))
                .thenReturn(this.expectedLeagueCategories.getFirst());
    }

    private void andFindLeagueCategoryByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leagueCategoriesClient).findLeagueCategoryById(anyString());
    }

    private void andFindAllLeagueCategoriesReturnedValidLeagues() {
        this.expectedLeagueCategories = List.of(new LeagueCategory("CALCIO", "Calcio Serie A",
                "Italian League", "FOOTBALL", "IT", "localhost",
                true, 123L, null));

        when(this.leagueCategoriesClient.findAllLeagueCategories())
                .thenReturn(this.expectedLeagueCategories);
    }

    private void andFindAllLeagueCategoriesThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leagueCategoriesClient).findAllLeagueCategories();
    }
}


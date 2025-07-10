package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.model.LeagueCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

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
@EntityScan("com.pmolinav.leagueslib.model")
class LeagueCategoryBOControllerIntegrationTest extends AbstractBaseTest {

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
    void createLeagueCategoryServerError() throws Exception {
        andCreateLeagueCategoryThrowsNonRetryableException();

        LeagueCategoryDTO requestDto = new LeagueCategoryDTO("CALCIO", "Calcio Serie A",
                "Italian League", "FOOTBALL", "IT", "localhost", true);

        mockMvc.perform(post("/categories?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createLeagueCategoryHappyPath() throws Exception {
        andCreateLeagueCategoryReturnedValidId();

        LeagueCategoryDTO requestDto = new LeagueCategoryDTO("CALCIO", "Calcio Serie A",
                "Italian League", "FOOTBALL", "IT", "localhost", true);

        MvcResult result = mockMvc.perform(post("/categories?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody, matchesPattern("\\w+"));
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

    @Test
    void deleteLeagueCategoryByIdInternalServerError() throws Exception {
        andLeagueCategoryDeleteThrowsNonRetryableException();

        mockMvc.perform(delete("/categories/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteLeagueCategoryByIdHappyPath() throws Exception {
        andLeagueCategoryIsDeletedOkOnClient();

        mockMvc.perform(delete("/categories/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }


    private void andLeagueCategoryIsDeletedOkOnClient() {
        doNothing().when(this.leaguesServiceClient).deleteLeagueCategory(anyString());
    }

    private void andLeagueCategoryDeleteThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).deleteLeagueCategory(anyString());
    }

    private void andFindLeagueCategoryByIdReturnedLeague() {
        this.expectedLeagueCategories = List.of(new LeagueCategory("CALCIO", "Calcio Serie A",
                "Italian League", "FOOTBALL", "IT", "localhost",
                true, 123L, null));

        when(this.leaguesServiceClient.findLeagueCategoryById(anyString()))
                .thenReturn(this.expectedLeagueCategories.getFirst());
    }

    private void andFindLeagueCategoryByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).findLeagueCategoryById(anyString());
    }

    private void andCreateLeagueCategoryReturnedValidId() {
        when(this.leaguesServiceClient.createLeagueCategory(any(LeagueCategoryDTO.class)))
                .thenReturn("CALCIO");
    }

    private void andCreateLeagueCategoryThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).createLeagueCategory(any(LeagueCategoryDTO.class));
    }

    private void andFindAllLeagueCategoriesReturnedValidLeagues() {
        this.expectedLeagueCategories = List.of(new LeagueCategory("CALCIO", "Calcio Serie A",
                "Italian League", "FOOTBALL", "IT", "localhost",
                true, 123L, null));

        when(this.leaguesServiceClient.findAllLeagueCategories())
                .thenReturn(this.expectedLeagueCategories);
    }

    private void andFindAllLeagueCategoriesThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).findAllLeagueCategories();
    }
}


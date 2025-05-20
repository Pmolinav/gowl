package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.model.LeagueStatus;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.leagueslib.model")
class LeagueBOControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<LeagueDTO> expectedLeagues;

    @Test
    void findAllLeaguesInternalServerError() throws Exception {
        andFindAllLeaguesThrowsNonRetryableException();

        mockMvc.perform(get("/leagues?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAllLeaguesHappyPath() throws Exception {
        andFindAllLeaguesReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/leagues?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<LeagueDTO> leagueResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<LeagueDTO>>() {
                });

        assertEquals(expectedLeagues, leagueResponseList);
    }

    @Test
    void createLeagueServerError() throws Exception {
        andCreateLeagueThrowsNonRetryableException();

        LeagueDTO requestDto = new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                22, null, false, "someUser");

        mockMvc.perform(post("/leagues?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createLeagueHappyPath() throws Exception {
        andCreateLeagueReturnedValidId();

        LeagueDTO requestDto = new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                22, null, false, "someUser");

        MvcResult result = mockMvc.perform(post("/leagues?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody, matchesPattern("\\d+"));
    }

    @Test
    void findLeagueByIdServerError() throws Exception {
        andFindLeagueByIdThrowsNonRetryableException();

        mockMvc.perform(get("/leagues/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findLeagueByIdHappyPath() throws Exception {
        andFindLeagueByIdReturnedLeague();

        MvcResult result = mockMvc.perform(get("/leagues/3?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        LeagueDTO leagueResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LeagueDTO>() {
                });

        assertEquals(expectedLeagues.getFirst(), leagueResponse);
    }

    @Test
    void findLeagueByNameHappyPath() throws Exception {
        andFindLeagueByNameReturnedLeague();

        MvcResult result = mockMvc.perform(get("/leagues/names/" + username + "?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        LeagueDTO leagueResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<LeagueDTO>() {
                });

        assertEquals(expectedLeagues.getFirst(), leagueResponse);
    }

    @Test
    void deleteLeagueByIdInternalServerError() throws Exception {
        andLeagueDeleteThrowsNonRetryableException();

        mockMvc.perform(delete("/leagues/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteLeagueByIdHappyPath() throws Exception {
        andLeagueIsDeletedOkOnClient();

        mockMvc.perform(delete("/leagues/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLeagueByNameInternalServerError() throws Exception {
        andLeagueDeleteByNameThrowsNonRetryableException();

        mockMvc.perform(delete("/leagues/names/fake league name?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteLeagueByNameHappyPath() throws Exception {
        andLeagueByNameIsDeletedOkOnClient();

        mockMvc.perform(delete("/leagues/names/Some League?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    private void andLeagueIsDeletedOkOnClient() {
        doNothing().when(this.leaguesClient).deleteLeague(anyLong());
    }
    private void andLeagueByNameIsDeletedOkOnClient() {
        doNothing().when(this.leaguesClient).deleteLeagueByName(anyString());
    }

    private void andLeagueDeleteThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.leaguesClient).deleteLeague(anyLong());
    }

    private void andLeagueDeleteByNameThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.leaguesClient).deleteLeagueByName(anyString());
    }

    private void andFindLeagueByIdReturnedLeague() {
        this.expectedLeagues = List.of(new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                22, null, false, "someUser"));

        when(this.leaguesClient.findLeagueById(anyLong())).thenReturn(this.expectedLeagues.getFirst());
    }

    private void andFindLeagueByNameReturnedLeague() {
        this.expectedLeagues = List.of(new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                22, null, false, "someUser"));

        when(this.leaguesClient.findLeagueByName(anyString())).thenReturn(this.expectedLeagues.getFirst());
    }

    private void andFindLeagueByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.leaguesClient).findLeagueById(anyLong());
    }

    private void andCreateLeagueReturnedValidId() {
        when(this.leaguesClient.createLeague(any(LeagueDTO.class))).thenReturn(1L);
    }

    private void andCreateLeagueThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.leaguesClient).createLeague(any(LeagueDTO.class));
    }

    private void andFindAllLeaguesReturnedValidLeagues() {
        this.expectedLeagues = List.of(new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                22, null, false, "someUser"));

        when(this.leaguesClient.findAllLeagues()).thenReturn(this.expectedLeagues);
    }

    private void andFindAllLeaguesThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.leaguesClient).findAllLeagues();
    }
}


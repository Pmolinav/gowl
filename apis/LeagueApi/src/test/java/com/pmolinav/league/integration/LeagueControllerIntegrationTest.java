package com.pmolinav.league.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeagueStatus;
import com.pmolinav.leagueslib.model.PlayerStatus;
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
class LeagueControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<LeagueDTO> expectedLeagues;

    @Test
    void createLeagueServerError() throws Exception {
        andCreateLeagueThrowsNonRetryableException();

        LeagueDTO requestDto = new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                22, null, false, "someUser", null);

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
                22, null, false, "someUser", null);

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
    void closeLeagueByIdServerError() throws Exception {
        andFindLeagueByIdReturnedLeague(1);
        andCloseLeagueByIdThrowsNonRetryableException();

        mockMvc.perform(put("/leagues/close/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void closeLeagueByIdHappyPath() throws Exception {
        andFindLeagueByIdReturnedLeague(1);
        andCloseLeagueByIdReturnedOk();

        mockMvc.perform(put("/leagues/close/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void closeLeagueByNameServerError() throws Exception {
        andFindLeagueByNameReturnedLeague();
        andCloseLeagueByNameThrowsNonRetryableException();

        mockMvc.perform(put("/leagues/close/names/Some League Name?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void closeLeagueByNameHappyPath() throws Exception {
        andFindLeagueByNameReturnedLeague();
        andCloseLeagueByNameReturnedOk();

        mockMvc.perform(put("/leagues/close/names/Some League?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
        andFindLeagueByIdReturnedLeague(3);

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

    private void andFindLeagueByIdReturnedLeague(long leagueId) {
        this.expectedLeagues = List.of(new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                22, null, false, "someUser",
                List.of(new LeaguePlayerDTO(leagueId, "someUser", 30, PlayerStatus.ACTIVE))));

        when(this.leaguesServiceClient.findLeagueById(anyLong())).thenReturn(this.expectedLeagues.getFirst());
    }

    private void andFindLeagueByNameReturnedLeague() {
        this.expectedLeagues = List.of(new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                22, null, false, "someUser",
                List.of(new LeaguePlayerDTO("someUser", 30, PlayerStatus.ACTIVE))));

        when(this.leaguesServiceClient.findLeagueByName(anyString())).thenReturn(this.expectedLeagues.getFirst());
    }

    private void andFindLeagueByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.leaguesServiceClient).findLeagueById(anyLong());
    }

    private void andCreateLeagueReturnedValidId() {
        when(this.leaguesServiceClient.createLeague(any(LeagueDTO.class))).thenReturn(1L);
    }

    private void andCreateLeagueThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.leaguesServiceClient).createLeague(any(LeagueDTO.class));
    }

    private void andCloseLeagueByIdReturnedOk() {
        doNothing().when(this.leaguesServiceClient).closeLeagueById(anyLong());
    }

    private void andCloseLeagueByNameReturnedOk() {
        doNothing().when(this.leaguesServiceClient).closeLeagueByName(anyString());
    }

    private void andCloseLeagueByIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).closeLeagueById(anyLong());
    }

    private void andCloseLeagueByNameThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).closeLeagueByName(anyString());
    }

}


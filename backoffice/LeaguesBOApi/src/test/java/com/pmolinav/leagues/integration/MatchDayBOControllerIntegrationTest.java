package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.model.MatchDayId;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.leagueslib.model")
class MatchDayBOControllerIntegrationTest extends AbstractBaseTest {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<MatchDayDTO> expectedMatchDays;
    private List<MatchDayId> expectedMatchDayIds;

    @Test
    void findAllMatchDaysInternalServerError() throws Exception {
        andFindAllMatchDaysThrowsNonRetryableException();

        mockMvc.perform(get("/match-days?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAllMatchDaysHappyPath() throws Exception {
        andFindAllMatchDaysReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/match-days?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<MatchDayDTO> matchDayResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDayDTO>>() {
                });

        assertEquals(expectedMatchDays, matchDayResponseList);
    }

    @Test
    void findMatchDaysByCategoryIdInternalServerError() throws Exception {
        andFindMatchDaysByCategoryIdThrowsNonRetryableException();

        mockMvc.perform(get("/match-days/categories/fakeCategory?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findMatchDaysByCategoryIdHappyPath() throws Exception {
        andFindMatchDaysByCategoryIdReturnedValidLeagues();

        MvcResult result = mockMvc.perform(get("/match-days/categories/PREMIER?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<MatchDayDTO> matchDayResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDayDTO>>() {
                });

        assertEquals(expectedMatchDays, matchDayResponseList);
    }

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

        List<MatchDayDTO> matchDayResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<MatchDayDTO>>() {
                });

        assertEquals(expectedMatchDays, matchDayResponseList);
    }

    @Test
    void createMatchDayServerError() throws Exception {
        andCreateMatchDayThrowsNonRetryableException();

        MatchDayDTO requestDto = new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L);

        mockMvc.perform(post("/match-days?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createMatchDayHappyPath() throws Exception {
        andCreateMatchDayReturnedValidId();

        MatchDayDTO requestDto = new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L);

        MvcResult result = mockMvc.perform(post("/match-days?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals(this.expectedMatchDayIds.getFirst(), objectMapper.readValue(responseBody, new TypeReference<MatchDayId>() {
        }));
    }

    @Test
    void createMatchDaysServerError() throws Exception {
        andCreateMatchDaysThrowsNonRetryableException();

        List<MatchDayDTO> requestDto = List.of(
                new MatchDayDTO("PREMIER", 2025,
                        10, 12345L, 12345678L)
        );

        mockMvc.perform(post("/match-days/bulk?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createMatchDaysHappyPath() throws Exception {
        andCreateMatchDaysReturnedValidIds();

        List<MatchDayDTO> requestDto = List.of(
                new MatchDayDTO("PREMIER", 2025,
                        4, 12345L, 12345678L),
                new MatchDayDTO("PREMIER", 2025,
                        5, 123456L, 123456789L)
        );

        MvcResult result = mockMvc.perform(post("/match-days/bulk?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals(this.expectedMatchDayIds, objectMapper.readValue(responseBody, new TypeReference<List<MatchDayId>>() {
        }));
    }

    @Test
    void deleteMatchDaysByCategoryIdInternalServerError() throws Exception {
        andMatchDayDeleteByCategoryIdThrowsNonRetryableException();

        mockMvc.perform(delete("/match-days/categories/fakeCategory?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteMatchDaysByCategoryIddHappyPath() throws Exception {
        andMatchDaysAreDeletedOkOnClient();

        mockMvc.perform(delete("/match-days/categories/PREMIER?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMatchDaysByCategoryIdAndSeasonInternalServerError() throws Exception {
        andMatchDayDeleteByCategoryIdAndSeasonThrowsNonRetryableException();

        mockMvc.perform(delete("/match-days/categories/fakeCategory/seasons/2010?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteMatchDaysByCategoryIdAndSeasonHappyPath() throws Exception {
        andMatchDaysAreDeletedOkOnClient();

        mockMvc.perform(delete("/match-days/categories/PREMIER/seasons/2025?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberInternalServerError() throws Exception {
        andMatchDayDeleteByCategoryIdSeasonAndNumberThrowsNonRetryableException();

        mockMvc.perform(delete("/match-days/categories/fakeCategory/seasons/2000/number/1?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberHappyPath() throws Exception {
        andMatchDaysAreDeletedOkOnClient();

        mockMvc.perform(delete("/match-days/categories/PREMIER/seasons/2025/number/3?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    private void andMatchDaysAreDeletedOkOnClient() {
        doNothing().when(this.leaguesServiceClient).deleteMatchDaysByCategoryId(anyString());
        doNothing().when(this.leaguesServiceClient).deleteMatchDaysByCategoryIdAndSeason(anyString(), anyInt());
        doNothing().when(this.leaguesServiceClient).deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void andMatchDayDeleteByCategoryIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).deleteMatchDaysByCategoryId(anyString());
    }

    private void andMatchDayDeleteByCategoryIdAndSeasonThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).deleteMatchDaysByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void andMatchDayDeleteByCategoryIdSeasonAndNumberThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void andCreateMatchDayReturnedValidId() {
        this.expectedMatchDayIds = List.of(
                new MatchDayId("PREMIER", 2025, 4)
        );

        when(this.leaguesServiceClient.createMatchDay(any(MatchDayDTO.class)))
                .thenReturn(this.expectedMatchDayIds.getFirst());
    }

    private void andCreateMatchDaysReturnedValidIds() {
        this.expectedMatchDayIds = List.of(
                new MatchDayId("PREMIER", 2025, 4),
                new MatchDayId("PREMIER", 2025, 5)
        );

        when(this.leaguesServiceClient.createMatchDays(anyList()))
                .thenReturn(this.expectedMatchDayIds);
    }

    private void andCreateMatchDayThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).createMatchDay(any(MatchDayDTO.class));
    }

    private void andCreateMatchDaysThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).createMatchDays(anyList());
    }

    private void andFindAllMatchDaysReturnedValidLeagues() {
        this.expectedMatchDays = List.of(new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L));

        when(this.leaguesServiceClient.findAllMatchDays())
                .thenReturn(this.expectedMatchDays);
    }

    private void andFindMatchDaysByCategoryIdReturnedValidLeagues() {
        this.expectedMatchDays = List.of(new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L));

        when(this.leaguesServiceClient.findMatchDayByCategoryId(anyString()))
                .thenReturn(this.expectedMatchDays);
    }

    private void andFindMatchDaysByCategoryIdAndSeasonReturnedValidLeagues() {
        this.expectedMatchDays = List.of(new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L));

        when(this.leaguesServiceClient.findMatchDayByCategoryIdAndSeason(anyString(), anyInt()))
                .thenReturn(this.expectedMatchDays);
    }

    private void andFindAllMatchDaysThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).findAllMatchDays();
    }

    private void andFindMatchDaysByCategoryIdThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).findMatchDayByCategoryId(anyString());
    }

    private void andFindMatchDaysByCategoryIdAndSeasonThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException"))
                .when(this.leaguesServiceClient).findMatchDayByCategoryIdAndSeason(anyString(), anyInt());
    }
}


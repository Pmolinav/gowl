package com.pmolinav.matchdatasync.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.clients.MatchDaysClient;
import com.pmolinav.matchdatasync.services.ExternalCategoryMappingService;
import com.pmolinav.matchdatasync.services.MatchDataSyncService;
import com.pmolinav.predictionslib.dto.MatchStatus;
import com.pmolinav.predictionslib.model.ExternalCategoryMapping;
import com.pmolinav.predictionslib.model.Match;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "external.api.url=http://localhost:9561" // override base url to WireMock
})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EntityScan("com.pmolinav.predictionslib.model")
class MatchDataSyncServiceIntegrationTest extends AbstractContainerBaseTest {

    @MockBean
    private MatchDaysClient matchDaysClient;

    @Autowired
    private MatchDataSyncService syncService;

    @Autowired
    private ExternalCategoryMappingService categoryMappingService;

    private static WireMockServer wireMockServer;

    private final String categoryId = "LA_LIGA";

    @BeforeAll
    void setupWireMock() {
        wireMockServer = new WireMockServer(9561);
        wireMockServer.start();
    }

    @AfterAll
    void teardownWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void scheduledMatchDaySyncWithNoStoredMatchesTest() {
        // 1. Save ExternalCategoryMapping for caching.
        ExternalCategoryMapping mapping = new ExternalCategoryMapping(categoryId, "soccer_spain_la_liga");
        mappingRepository.save(mapping);

        andRealResponseIsObtainedFromExternalApi();

        // 2. Mock MatchDaysClient to return a MatchDayDTO.
        long now = System.currentTimeMillis();
        List<MatchDayDTO> matchDayDTOList = List.of(
                new MatchDayDTO(categoryId, 2025, 1, now + 1000L, now + 2000L)
        );

        when(matchDaysClient.findAllMatchDays(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(matchDayDTOList);

        // 3. Run the scheduled sync method manually.
        syncService.scheduleMatchDaySync();

        // 4. Verify that matches were saved from WireMock stubbed response.
        List<Match> matches = matchRepository.findAll();
        assertEquals(2, matches.stream()
                .filter(match1 -> match1.getStatus().equals(MatchStatus.ACTIVE)
                        && match1.getExternalId() != null)
                .toList().size());
        assertEquals(categoryId, matches.getFirst().getCategoryId());
    }

    @Test
    void scheduledMatchDaySyncWithSomeMatchesStoredPreviouslyTest() {
        // 1. Set the startTime for previously stored match (the same as response JSON).
        long millis = OffsetDateTime.parse("2025-04-22T17:00:00Z")
                .toInstant()
                .toEpochMilli();
        // 2. Save one of the expected matches as SCHEDULED previously on the database.
        Match match = new Match(null, "LA_LIGA", 2025, 1,
                "Valencia", "Espanyol", millis, MatchStatus.SCHEDULED,
                null, null);
        matchRepository.save(match);

        // 3. Save ExternalCategoryMapping for caching.
        ExternalCategoryMapping mapping = new ExternalCategoryMapping(categoryId, "soccer_spain_la_liga");
        mappingRepository.save(mapping);

        andRealResponseIsObtainedFromExternalApi();

        // 4. Mock MatchDaysClient to return a MatchDayDTO.
        long now = System.currentTimeMillis();
        List<MatchDayDTO> matchDayDTOList = List.of(
                new MatchDayDTO(categoryId, 2025, 1, now + 1000L, now + 2000L)
        );

        when(matchDaysClient.findAllMatchDays(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(matchDayDTOList);

        // 5. Run the scheduled sync method manually.
        syncService.scheduleMatchDaySync();

        // 6. Verify that matches were saved from WireMock stubbed response.
        List<Match> matches = matchRepository.findAll();
        assertEquals(2, matches.stream()
                .filter(match1 -> match1.getStatus().equals(MatchStatus.ACTIVE)
                        && match1.getExternalId() != null)
                .toList().size());
        assertEquals(categoryId, matches.getFirst().getCategoryId());
    }

    @Test
    void scheduledMatchDaySyncWithSomeActiveMatchesStoredPreviouslyTest() {
        // 1. Set the startTime for previously stored match (the same as response JSON).
        // Due to hours' margin, it should be found successfully.
        long millis = OffsetDateTime.parse("2025-04-22T17:00:00Z")
                .toInstant()
                .toEpochMilli();
        // 2. Save one of the expected matches as ACTIVE previously on the database.
        Match match = new Match(null, "LA_LIGA", 2025, 1,
                "Valencia", "Espanyol", millis, MatchStatus.ACTIVE,
                "78ee12a03a3be4eca4b74ed1a2d78ba8", null, null);
        matchRepository.save(match);

        // 3. Save the other expected match as SCHEDULED previously on the database.
        Match match2 = new Match(null, "LA_LIGA", 2025, 1,
                "Barcelona", "Mallorca", millis, MatchStatus.SCHEDULED,
                null, null);
        matchRepository.save(match2);

        // 4. Save ExternalCategoryMapping for caching.
        ExternalCategoryMapping mapping = new ExternalCategoryMapping(categoryId, "soccer_spain_la_liga");
        mappingRepository.save(mapping);

        andRealResponseIsObtainedFromExternalApi();

        // 5. Mock MatchDaysClient to return a MatchDayDTO.
        long now = System.currentTimeMillis();
        List<MatchDayDTO> matchDayDTOList = List.of(
                new MatchDayDTO(categoryId, 2025, 1, now + 1000L, now + 2000L)
        );

        when(matchDaysClient.findAllMatchDays(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(matchDayDTOList);

        // 6. Run the scheduled sync method manually.
        syncService.scheduleMatchDaySync();

        // 7. Verify that matches were saved from WireMock stubbed response.
        List<Match> matches = matchRepository.findAll();
        assertEquals(2, matches.stream()
                .filter(match1 -> match1.getStatus().equals(MatchStatus.ACTIVE)
                        && match1.getExternalId() != null)
                .toList().size());
        assertEquals(categoryId, matches.getFirst().getCategoryId());
    }

    private void andRealResponseIsObtainedFromExternalApi() {
        // Expected response with real data from external API.
        String expectedResponse = readJsonFromResource("utils/response.json");

        // Stub the external odds API endpoint matching your WebClient path
        wireMockServer.stubFor(WireMock.get(
                        WireMock.urlPathMatching("/sports/soccer_spain_la_liga/odds.*"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(expectedResponse)));
    }
}
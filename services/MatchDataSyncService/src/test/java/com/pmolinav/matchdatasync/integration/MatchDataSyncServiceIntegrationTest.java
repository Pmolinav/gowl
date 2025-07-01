package com.pmolinav.matchdatasync.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.clients.MatchDaysClient;
import com.pmolinav.matchdatasync.services.ExternalCategoryMappingService;
import com.pmolinav.matchdatasync.services.MatchDataSyncService;
import com.pmolinav.predictionslib.dto.EventType;
import com.pmolinav.predictionslib.dto.MatchStatus;
import com.pmolinav.predictionslib.dto.PlayerBetStatus;
import com.pmolinav.predictionslib.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @BeforeEach
    void beforeEach() {
        eventsPreviouslyStored();
    }

    private void eventsPreviouslyStored() {
        Event h2h = new Event(EventType.H2H.getName(), EventType.H2H.getDescription(),
                12345L, 12345L);
        eventRepository.save(h2h);
        Event totals = new Event(EventType.TOTALS.getName(), EventType.TOTALS.getDescription(),
                12345L, 12345L);
        eventRepository.save(totals);
        Event spreads = new Event(EventType.SPREADS.getName(), EventType.SPREADS.getDescription(),
                12345L, 12345L);
        eventRepository.save(spreads);
    }

    @AfterAll
    void teardownWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
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

    @Test
    void scheduledPlayerBetResultCheckTest() {
        // Invalidate all caches previously.
        try {
            mockMvc.perform(delete("/caches/events"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail();
        }
        // 1. Execute the scheduled process to store real matches, events and odds.
        scheduledMatchDaySyncWithNoStoredMatchesTest();

        // 2. Set up a completed MatchDay (results not checked yet)
        long now = System.currentTimeMillis();
        MatchDayDTO completedMatchDay = new MatchDayDTO(categoryId, 2025, 1,
                now - 20000L, now - 10000L, true, false);

        // 3. Save ExternalCategoryMapping to resolve the external sport key
        ExternalCategoryMapping mapping = new ExternalCategoryMapping(categoryId, "soccer_spain_la_liga");
        mappingRepository.save(mapping);

        // 4. Insert some examples of player bets and selections for existing resources.
        givenDataPreviouslyStored(categoryId, 2025, 1);

        // 5. Stub external results endpoint (WireMock)
        andRealResultsAreObtainedFromExternalApi();

        // 6. Mock MatchDaysClient to return completed match day
        when(matchDaysClient.findCompletedMatchDays(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(List.of(completedMatchDay));

        // 7. Mock MatchDaysClient update method to verify update gets triggered
        doNothing().when(matchDaysClient).updateMatchDay(any(MatchDayDTO.class));

        // 8. Run the scheduled result check manually
        syncService.schedulePlayerBetResultCheck();

        // 9. Verify updateMatchDay was called
        verify(matchDaysClient, times(1)).updateMatchDay(argThat(md ->
                md.getCategoryId().equals(categoryId) && md.isResultsChecked()
        ));

        // 10. Verify 2 WON and 2 LOST PlayerBets.
        List<PlayerBet> allBets = playerBetRepository.findAll();
        assertEquals(2, allBets.stream()
                .filter(playerBet -> PlayerBetStatus.WON.equals(playerBet.getStatus()))
                .toList().size());

        assertEquals(2, allBets.stream()
                .filter(playerBet -> PlayerBetStatus.LOST.equals(playerBet.getStatus()))
                .toList().size());

        // 11. Verify 4 WON and 3 LOST PlayerBet selections.
        List<PlayerBetSelection> allSelections = playerBetSelectionRepository.findAll();

        assertEquals(4, allSelections.stream()
                .filter(selection -> PlayerBetStatus.WON.equals(selection.getStatus()))
                .toList().size());

        assertEquals(3, allSelections.stream()
                .filter(selection -> PlayerBetStatus.LOST.equals(selection.getStatus()))
                .toList().size());
    }

    //TODO: REVISAR POR QUE FALLA AL TIRAR TODOS
    private void scheduledMatchDaySyncWithNoStoredMatchesTest() {
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

    private void andRealResponseIsObtainedFromExternalApi() {
        // Expected response with real data from external API.
        String expectedResponse = readJsonFromResource("utils/matches_events_response.json");

        // Stub the external odds API endpoint matching your WebClient path
        wireMockServer.stubFor(WireMock.get(
                        WireMock.urlPathMatching("/sports/soccer_spain_la_liga/odds.*"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(expectedResponse)));
    }

    private void givenDataPreviouslyStored(String categoryId, int season, int number) {
        // Search matches (two matches expected).
        List<Match> matches = matchRepository.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, number);

        // Get created odds by events (event types are assumed).
        List<Odds> oddsForH2H = oddsRepository.findByEventType(EventType.H2H.getName());
        List<Odds> oddsForSpreads = oddsRepository.findByEventType(EventType.SPREADS.getName());
        List<Odds> oddsForTotals = oddsRepository.findByEventType(EventType.TOTALS.getName());

        // Two player bet for the first match found.
        // WON
        PlayerBet bet1 = new PlayerBet(null, "someUser", matches.getFirst().getMatchId(), 1L,
                BigDecimal.valueOf(2.20), PlayerBetStatus.SUBMITTED, 12345L);
        bet1 = playerBetRepository.save(bet1);

        // LOST
        PlayerBet bet2 = new PlayerBet(null, "otherUser", matches.getFirst().getMatchId(), 1L,
                BigDecimal.valueOf(5.20), PlayerBetStatus.SUBMITTED, 12345L);
        bet2 = playerBetRepository.save(bet2);

        // Two players bet for the second match found.
        // WON
        PlayerBet bet3 = new PlayerBet(null, "someUser", matches.getLast().getMatchId(), 1L,
                BigDecimal.valueOf(3.30), PlayerBetStatus.SUBMITTED, 12345L);
        bet3 = playerBetRepository.save(bet3);

        // LOST
        PlayerBet bet4 = new PlayerBet(null, "otherUser", matches.getLast().getMatchId(), 1L,
                BigDecimal.valueOf(5.20), PlayerBetStatus.SUBMITTED, 12345L);
        bet4 = playerBetRepository.save(bet4);

        // The First player bet for Valencia in the first match.
        // WON.
        PlayerBetSelection sel1 = new PlayerBetSelection(null, bet1.getBetId(), EventType.H2H.getName(),
                oddsForH2H.stream().filter(odds -> "Valencia".equals(odds.getLabel()))
                        .findFirst().get().getOddsId(), BigDecimal.valueOf(1.50), PlayerBetStatus.SUBMITTED, 1234567L);

        // The Second player bet for Under 2.5 goals in the first match.
        // LOST.
        PlayerBetSelection sel2 = new PlayerBetSelection(null, bet2.getBetId(), EventType.TOTALS.getName(),
                oddsForTotals.stream().filter(odds -> "Under".equals(odds.getLabel()) &&
                                new BigDecimal("2.50").equals(odds.getPoint()))
                        .findFirst().get().getOddsId(), BigDecimal.TWO, PlayerBetStatus.SUBMITTED, 1234567L);

        // The First player bet for Barcelona, spreads and over 3.5 goals in the second match.
        // WON.
        PlayerBetSelection sel3 = new PlayerBetSelection(null, bet3.getBetId(), EventType.H2H.getName(),
                oddsForH2H.stream().filter(odds -> "Barcelona".equals(odds.getLabel()))
                        .findFirst().get().getOddsId(), BigDecimal.valueOf(1.20), PlayerBetStatus.SUBMITTED, 1234567L);

        // WON
        PlayerBetSelection sel4 = new PlayerBetSelection(null, bet3.getBetId(), EventType.SPREADS.getName(),
                oddsForSpreads.stream().filter(odds -> "Barcelona".equals(odds.getLabel()))
                        .findFirst().get().getOddsId(), BigDecimal.TWO, PlayerBetStatus.SUBMITTED, 1234567L);
        // WON
        PlayerBetSelection sel5 = new PlayerBetSelection(null, bet3.getBetId(), EventType.TOTALS.getName(),
                oddsForTotals.stream().filter(odds -> "Over".equals(odds.getLabel()) &&
                                new BigDecimal("3.50").equals(odds.getPoint()))
                        .findFirst().get().getOddsId(), BigDecimal.TWO, PlayerBetStatus.SUBMITTED, 1234567L);

        // The Second player bet for Mallorca and Under 3.5 goals.
        // LOST
        PlayerBetSelection sel6 = new PlayerBetSelection(null, bet4.getBetId(), EventType.H2H.getName(),
                oddsForH2H.stream().filter(odds -> "Mallorca".equals(odds.getLabel()))
                        .findFirst().get().getOddsId(), BigDecimal.valueOf(5.40), PlayerBetStatus.SUBMITTED, 1234567L);

        // LOST
        PlayerBetSelection sel7 = new PlayerBetSelection(null, bet4.getBetId(), EventType.TOTALS.getName(),
                oddsForTotals.stream().filter(odds -> "Under".equals(odds.getLabel()) &&
                                new BigDecimal("3.50").equals(odds.getPoint()))
                        .findFirst().get().getOddsId(), BigDecimal.TWO, PlayerBetStatus.SUBMITTED, 1234567L);

        playerBetSelectionRepository.saveAll(List.of(sel1, sel2, sel3, sel4, sel5, sel6, sel7));
    }

    private void andRealResultsAreObtainedFromExternalApi() {
        String response = readJsonFromResource("utils/matches_results_response.json");

        wireMockServer.stubFor(WireMock.get(
                        WireMock.urlPathMatching("/sports/soccer_spain_la_liga/scores.*"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(response)));
    }

}
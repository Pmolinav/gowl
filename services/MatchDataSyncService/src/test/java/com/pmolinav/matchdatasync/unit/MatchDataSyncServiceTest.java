package com.pmolinav.matchdatasync.unit;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.clients.ExternalMatchClient;
import com.pmolinav.matchdatasync.clients.MatchDaysClient;
import com.pmolinav.matchdatasync.dto.ExternalMatchDTO;
import com.pmolinav.matchdatasync.dto.ExternalMatchScoreDTO;
import com.pmolinav.matchdatasync.dto.ExternalScoreDTO;
import com.pmolinav.matchdatasync.services.ExternalCategoryMappingService;
import com.pmolinav.matchdatasync.services.MatchDataProcessor;
import com.pmolinav.matchdatasync.services.MatchDataSyncService;
import com.pmolinav.matchdatasync.services.PlayerBetDataProcessor;
import com.pmolinav.predictionslib.model.ExternalCategoryMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchDataSyncServiceTest {

    @Mock
    private MatchDaysClient matchDaysClient;

    @Mock
    private ExternalMatchClient externalMatchClient;

    @Mock
    private ExternalCategoryMappingService externalCategoryMappingService;

    @Mock
    private MatchDataProcessor matchDataProcessor;

    @Mock
    private PlayerBetDataProcessor playerBetDataProcessor;

    @InjectMocks
    private MatchDataSyncService matchDataSyncService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(matchDataSyncService, "offsetStartMs", 100000L);
        ReflectionTestUtils.setField(matchDataSyncService, "offsetEndMs", 100000L);
        ReflectionTestUtils.setField(matchDataSyncService, "apiKey", "test-api-key");
    }

    @Test
    void shouldSyncMatchDaysSuccessfully() {
        // Given
        long now = System.currentTimeMillis();
        long expectedDateTo = now + 100000L;
        String categoryId = "LA_LIGA";

        MatchDayDTO matchDay = new MatchDayDTO(categoryId, 2025, 1, now, expectedDateTo);

        ExternalCategoryMapping mapping = new ExternalCategoryMapping(categoryId, "soccer_spain_la_liga");

        ExternalMatchDTO match = new ExternalMatchDTO("ext-id", "soccer_spain_la_liga", "La Liga",
                OffsetDateTime.now(), "Valencia", "Barcelona", List.of()
        );

        List<ExternalMatchDTO> matches = List.of(match);

        when(matchDaysClient.findAllMatchDays(anyLong(), anyLong(), eq(false)))
                .thenReturn(List.of(matchDay));
        when(externalCategoryMappingService.cachedFindById(categoryId))
                .thenReturn(mapping);
        when(externalMatchClient.fetchOdds(matchDay, "soccer_spain_la_liga", "test-api-key"))
                .thenReturn(matches);
        when(matchDataProcessor.processMatches(any(MatchDayDTO.class), anyList()))
                .thenReturn(true);

        // When
        matchDataSyncService.scheduleMatchDaySync();

        // Then
        verify(matchDaysClient).findAllMatchDays(anyLong(), anyLong(), eq(false));
        verify(externalCategoryMappingService).cachedFindById(categoryId);
        verify(externalMatchClient).fetchOdds(matchDay, "soccer_spain_la_liga", "test-api-key");
        verify(matchDataProcessor).processMatches(matchDay, matches);
        verify(matchDaysClient).updateMatchDay(matchDay);

        assertTrue(matchDay.isSynced());
    }

    @Test
    void shouldCheckPlayerBetResultsSuccessfully() {
        // Given
        long now = System.currentTimeMillis();
        String categoryId = "LA_LIGA";
        MatchDayDTO matchDay = new MatchDayDTO(categoryId, 2025, 1,
                now - 100000, now - 50000, true, false);
        matchDay.setResultsChecked(false);

        ExternalCategoryMapping mapping = new ExternalCategoryMapping(categoryId, "soccer_spain_la_liga");

        ExternalMatchScoreDTO result = new ExternalMatchScoreDTO();
        result.setId("ext-id");
        result.setScores(List.of(
                new ExternalScoreDTO("Valencia", "1"),
                new ExternalScoreDTO("Barcelona", "2")
        ));

        List<ExternalMatchScoreDTO> results = List.of(result);

        when(matchDaysClient.findCompletedMatchDays(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(List.of(matchDay));
        when(externalCategoryMappingService.cachedFindById(categoryId))
                .thenReturn(mapping);
        when(externalMatchClient.fetchResults("soccer_spain_la_liga", "test-api-key", 3))
                .thenReturn(results);
        when(playerBetDataProcessor.processResults(matchDay, results))
                .thenReturn(true);

        // When
        matchDataSyncService.schedulePlayerBetResultCheck();

        // Then
        verify(matchDaysClient).findCompletedMatchDays(anyLong(), anyLong(), anyBoolean());
        verify(externalCategoryMappingService).cachedFindById(categoryId);
        verify(externalMatchClient).fetchResults("soccer_spain_la_liga", "test-api-key", 3);
        verify(playerBetDataProcessor).processResults(matchDay, results);
        verify(matchDaysClient).updateMatchDay(matchDay);

        assertTrue(matchDay.isResultsChecked());
    }

}


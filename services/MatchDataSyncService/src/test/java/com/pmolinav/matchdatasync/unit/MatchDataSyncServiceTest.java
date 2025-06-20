package com.pmolinav.matchdatasync.unit;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.clients.ExternalMatchClient;
import com.pmolinav.matchdatasync.clients.MatchDaysClient;
import com.pmolinav.matchdatasync.dto.ExternalMatchDTO;
import com.pmolinav.matchdatasync.services.ExternalCategoryMappingService;
import com.pmolinav.matchdatasync.services.MatchDataProcessor;
import com.pmolinav.matchdatasync.services.MatchDataSyncService;
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

    @InjectMocks
    private MatchDataSyncService matchDataSyncService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(matchDataSyncService, "offsetMs", 100000L);
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
}


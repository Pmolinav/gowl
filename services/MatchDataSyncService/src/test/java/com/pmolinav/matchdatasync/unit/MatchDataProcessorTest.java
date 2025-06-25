package com.pmolinav.matchdatasync.unit;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.dto.ExternalBookmakerDTO;
import com.pmolinav.matchdatasync.dto.ExternalMarketDTO;
import com.pmolinav.matchdatasync.dto.ExternalMatchDTO;
import com.pmolinav.matchdatasync.dto.ExternalOutcomeDTO;
import com.pmolinav.matchdatasync.exceptions.NotFoundException;
import com.pmolinav.matchdatasync.services.EventService;
import com.pmolinav.matchdatasync.services.MatchDataProcessor;
import com.pmolinav.matchdatasync.services.MatchService;
import com.pmolinav.matchdatasync.services.OddsService;
import com.pmolinav.predictionslib.dto.EventType;
import com.pmolinav.predictionslib.dto.MatchStatus;
import com.pmolinav.predictionslib.model.Event;
import com.pmolinav.predictionslib.model.Match;
import com.pmolinav.predictionslib.model.Odds;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchDataProcessorTest {

    @Mock
    private MatchService matchService;

    @Mock
    private EventService eventService;

    @Mock
    private OddsService oddsService;

    @InjectMocks
    private MatchDataProcessor matchDataProcessor;

    @Test
    void shouldProcessMatchAndPersistEntitiesCorrectly() {
        // Given
        ExternalMatchDTO externalMatch = buildSampleExternalMatch();
        MatchDayDTO matchDay = buildSampleMatchDayDTO();

        // Mocks
        andMatchServiceReturnsCreatedMatch();
        andEventServiceReturnsCreatedEvent();
        andOddsServiceReturnsCreatedEOdds();

        // When
        matchDataProcessor.processMatches(matchDay, List.of(externalMatch));

        // Then
        verify(matchService, times(1)).createMatch(any());
        verify(eventService, times(1)).createEvent(any());
        verify(oddsService, times(1)).createOddsList(any());
    }

    private ExternalMatchDTO buildSampleExternalMatch() {
        ExternalOutcomeDTO outcome1 = new ExternalOutcomeDTO("Valencia", BigDecimal.valueOf(1.87), null, null);
        ExternalOutcomeDTO outcome2 = new ExternalOutcomeDTO("Barcelona", BigDecimal.valueOf(4.83), null, null);
        ExternalOutcomeDTO outcome3 = new ExternalOutcomeDTO("Draw", BigDecimal.valueOf(3.64), null, null);

        ExternalMarketDTO market = new ExternalMarketDTO("h2h", OffsetDateTime.parse("2025-04-21T18:56:14Z"), null, List.of(outcome1, outcome2, outcome3));
        ExternalBookmakerDTO bookmaker = new ExternalBookmakerDTO("onexbet", "1xBet", OffsetDateTime.parse("2025-04-21T18:56:14Z"), "https://1xbet.com", List.of(market));

        return new ExternalMatchDTO(
                "78ee12a03a3be4eca4b74ed1a2d78ba8",
                "soccer_spain_la_liga",
                "La Liga - Spain",
                OffsetDateTime.parse("2025-04-22T17:00:00Z"),
                "Valencia",
                "Barcelona",
                List.of(bookmaker)
        );
    }

    private MatchDayDTO buildSampleMatchDayDTO() {
        return new MatchDayDTO("LA_LIGA", 2025, 1,
                1234567L, 123456789L);
    }

    private void andMatchServiceReturnsCreatedMatch() {
        Match match = new Match(1L, "LA_LIGA", 2025, 1,
                "Valencia", "Barcelona", 123456789L,
                MatchStatus.ACTIVE, null, null);

        when(this.matchService.createMatch(any()))
                .thenReturn(match);
    }

    private void andEventServiceReturnsCreatedEvent() {
        Event event = new Event(EventType.H2H.getName(), 2L,
                EventType.H2H.getDescription(), null, null);

        when(this.eventService.cachedFindEventByType(any()))
                .thenThrow(new NotFoundException("Not found"));

        when(this.eventService.createEvent(any()))
                .thenReturn(event);
    }

    private void andOddsServiceReturnsCreatedEOdds() {
        List<Odds> odds = List.of(
                new Odds(1L, EventType.H2H.getName(), "Valencia", BigDecimal.valueOf(3.20),
                        true, null, null),
                new Odds(2L, EventType.H2H.getName(), "Barcelona", BigDecimal.valueOf(1.80),
                        true, null, null),
                new Odds(3L, EventType.H2H.getName(), "Draw", BigDecimal.valueOf(2.10),
                        true, null, null)
        );

        when(this.oddsService.createOddsList(any()))
                .thenReturn(odds);
    }
}
package com.pmolinav.predictionslib;

import com.pmolinav.predictionslib.dto.*;
import com.pmolinav.predictionslib.mapper.*;
import com.pmolinav.predictionslib.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappersTests {

    private final MatchMapper matchMapper = MatchMapper.INSTANCE;
    private final EventMapper eventMapper = EventMapper.INSTANCE;
    private final OddsMapper oddsMapper = OddsMapper.INSTANCE;
    private final PlayerBetMapper playerBetMapper = PlayerBetMapper.INSTANCE;
    private final PlayerBetSelectionMapper playerBetSelectionMapper = PlayerBetSelectionMapper.INSTANCE;

    @Test
    void matchDtoToEntityTest() {
        MatchDTO dto = new MatchDTO("PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, MatchStatus.SCHEDULED);

        Match expected = new Match(1L, "PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, MatchStatus.SCHEDULED, null, null);

        Match actual = matchMapper.matchDtoToEntity(dto);
        actual.setMatchId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void matchEntityToDtoTest() {
        Match entity = new Match(1L, "PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, MatchStatus.SCHEDULED, null, null);

        MatchDTO expected = new MatchDTO("PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, MatchStatus.SCHEDULED);

        MatchDTO actual = matchMapper.matchEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void matchEntityToSimpleDtoTest() {
        Match entity = new Match(1L, "PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, MatchStatus.SCHEDULED, null, null);

        SimpleMatchDTO expected = new SimpleMatchDTO("Team A", "Team B",
                123456789L, MatchStatus.SCHEDULED);

        SimpleMatchDTO actual = matchMapper.matchEntityToSimpleDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void matchDTOToSimpleDtoTest() {
        MatchDTO dto = new MatchDTO("PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, MatchStatus.SCHEDULED);

        SimpleMatchDTO expected = new SimpleMatchDTO("Team A", "Team B",
                123456789L, MatchStatus.SCHEDULED);

        SimpleMatchDTO actual = matchMapper.matchDTOToSimpleDto(dto);

        assertEquals(expected, actual);
    }

    @Test
    void eventDtoToEntityTest() {
        EventDTO dto = new EventDTO(EventType.H2H.getName(), EventType.H2H.getDescription());

        Event expected = new Event(EventType.H2H.getName(), EventType.H2H.getDescription(), null, null);

        Event actual = eventMapper.eventDtoToEntity(dto);

        assertEquals(expected, actual);
    }

    @Test
    void eventEntityToDtoTest() {
        Event entity = new Event(EventType.H2H.getName(), EventType.H2H.getDescription(), 123L, 456L);

        EventDTO expected = new EventDTO(EventType.H2H.getName(), EventType.H2H.getDescription());

        EventDTO actual = eventMapper.eventEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void oddsDtoToEntityTest() {
        OddsDTO dto = new OddsDTO(EventType.H2H.getName(), 1L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(1.5), null, true);

        Odds expected = new Odds(1L, EventType.H2H.getName(), 1L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(1.5), null, true, null, null);

        Odds actual = oddsMapper.oddsDtoToEntity(dto);
        actual.setOddsId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void oddsEntityToDtoTest() {
        Odds entity = new Odds(2L, EventType.H2H.getName(), 1L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(2.5), "provider", true, 1000L, 2000L);

        OddsDTO expected = new OddsDTO(EventType.H2H.getName(), 1L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(2.5), "provider", true);

        OddsDTO actual = oddsMapper.oddsEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void oddsEntityToSimpleDtoTest() {
        Odds entity = new Odds(2L, EventType.H2H.getName(), 1L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(2.5), "provider", true, 1000L, 2000L);

        SimpleOddsDTO expected = new SimpleOddsDTO(EventType.H2H.getName(), "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(2.5), true);

        SimpleOddsDTO actual = oddsMapper.oddsEntityToSimpleDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void oddsDTOToSimpleDtoTest() {
        OddsDTO dto = new OddsDTO(EventType.H2H.getName(), 1L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(1.5), null, true);

        SimpleOddsDTO expected = new SimpleOddsDTO(EventType.H2H.getName(), "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(1.5), true);

        SimpleOddsDTO actual = oddsMapper.oddsDTOToSimpleDto(dto);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetDtoToEntityTest() {
        PlayerBetDTO dto = new PlayerBetDTO("user123", 3L, 1L, BigDecimal.TEN, null);

        PlayerBet expected = new PlayerBet(1L, "user123", 3L, 1L, BigDecimal.TEN, null);

        PlayerBet actual = playerBetMapper.playerBetDtoToEntity(dto);
        actual.setBetId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetEntityToDtoTest() {
        PlayerBet entity = new PlayerBet(1L, "user123", 3L, 1L, BigDecimal.TEN, 123456789L);

        PlayerBetDTO expected = new PlayerBetDTO("user123", 3L, 1L, BigDecimal.TEN, 123456789L, null);

        PlayerBetDTO actual = playerBetMapper.playerBetEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetDtoToEntityWithSelectionTest() {
        PlayerBetDTO dto = new PlayerBetDTO("user123", 3L, 1L,
                BigDecimal.TEN, List.of(
                new PlayerBetSelectionDTO(EventType.H2H.getName(), 3L, BigDecimal.ONE),
                new PlayerBetSelectionDTO(EventType.H2H.getName(), 4L, BigDecimal.TWO)
        ));

        PlayerBet expected = new PlayerBet(1L, "user123",
                3L, 1L, BigDecimal.TEN, 123456789L);
        expected.setSelections(List.of(
                new PlayerBetSelection(11L, 1L, EventType.H2H.getName(), 3L,
                        BigDecimal.ONE, 123456789L),
                new PlayerBetSelection(11L, 1L, EventType.H2H.getName(), 4L,
                        BigDecimal.TWO, 123456789L)
        ));

        PlayerBet actual = playerBetMapper.playerBetDtoToEntity(dto);
        actual.setBetId(1L);
        actual.setSelections(actual.getSelections().stream()
                .map(playerBetSelection -> new PlayerBetSelection(
                        11L, 1L, playerBetSelection.getEventType(), playerBetSelection.getOddsId(),
                        playerBetSelection.getStake(), playerBetSelection.getCreationDate())).toList());

        assertEquals(expected, actual);
    }

    @Test
    void playerBetEntityToDtoWithSelectionsTest() {
        PlayerBet entity = new PlayerBet(1L, "user123",
                3L, 1L, BigDecimal.TEN, 123456789L);
        entity.setSelections(List.of(
                new PlayerBetSelection(11L, 1L, EventType.H2H.getName(), 3L,
                        BigDecimal.ONE, 123456789L),
                new PlayerBetSelection(11L, 1L, EventType.H2H.getName(), 4L,
                        BigDecimal.TWO, 123456789L)
        ));

        PlayerBetDTO expected = new PlayerBetDTO("user123", 3L, 1L,
                BigDecimal.TEN, 123456789L, List.of(
                new PlayerBetSelectionDTO(EventType.H2H.getName(), 3L, BigDecimal.ONE),
                new PlayerBetSelectionDTO(EventType.H2H.getName(), 4L, BigDecimal.TWO)
        ));

        PlayerBetDTO actual = playerBetMapper.playerBetEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetEntityToByUsernameDtoTest() {
        Match match = new Match(1L, "PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, MatchStatus.SCHEDULED,
                null, null);

        PlayerBetSelection selection1 = new PlayerBetSelection(11L, 1L, EventType.H2H.getName(), 3L,
                BigDecimal.ONE, 123456789L);
        selection1.setOdds(new Odds(2L, EventType.H2H.getName(), 1L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(2.5), "provider", true, 1000L, 2000L));

        PlayerBetSelection selection2 = new PlayerBetSelection(11L, 1L, EventType.H2H.getName(), 4L,
                BigDecimal.TWO, 123456789L);
        selection2.setOdds(new Odds(2L, EventType.H2H.getName(), 1L, "Under", BigDecimal.valueOf(2.20),
                BigDecimal.valueOf(2.5), "provider", true, 1000L, 2000L));

        PlayerBet entity = new PlayerBet(1L, "user123",
                3L, 1L, BigDecimal.TEN, 123456789L);

        entity.setSelections(List.of(selection1, selection2));

        entity.setMatch(match);

        PlayerBetByUsernameDTO expected = new PlayerBetByUsernameDTO("user123",
                matchMapper.matchEntityToSimpleDto(match), 1L,
                BigDecimal.TEN, 123456789L, List.of(
                playerBetSelectionMapper.playerBetEntityToByUsernameDtoList(selection1),
                playerBetSelectionMapper.playerBetEntityToByUsernameDtoList(selection2))
        );

        PlayerBetByUsernameDTO actual = playerBetMapper.playerBetEntityToByUsernameDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetSelectionDtoToEntityTest() {
        PlayerBetSelectionDTO dto = new PlayerBetSelectionDTO(EventType.TOTALS.getName(), 3L, BigDecimal.valueOf(100.50));

        PlayerBetSelection expected = new PlayerBetSelection(1L, 2L, EventType.TOTALS.getName(),
                3L, BigDecimal.valueOf(100.50), null);

        PlayerBetSelection actual = playerBetSelectionMapper.playerBetSelectionDtoToEntity(dto);
        actual.setSelectionId(1L);
        actual.setBetId(2L);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetSelectionEntityToDtoTest() {
        PlayerBetSelection entity = new PlayerBetSelection(1L, 2L, EventType.SPREADS.getName(),
                3L, BigDecimal.valueOf(100.50), 555555L);

        PlayerBetSelectionDTO expected = new PlayerBetSelectionDTO(EventType.SPREADS.getName(), 3L, BigDecimal.valueOf(100.50));

        PlayerBetSelectionDTO actual = playerBetSelectionMapper.playerBetSelectionEntityToDto(entity);

        assertEquals(expected, actual);
    }
}
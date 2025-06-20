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
                "Team A", "Team B", 123456789L, "SCHEDULED");

        Match expected = new Match(1L, "PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, "SCHEDULED", null, null);

        Match actual = matchMapper.matchDtoToEntity(dto);
        actual.setMatchId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void matchEntityToDtoTest() {
        Match entity = new Match(1L, "PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, "SCHEDULED", null, null);

        MatchDTO expected = new MatchDTO("PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, "SCHEDULED");

        MatchDTO actual = matchMapper.matchEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void eventDtoToEntityTest() {
        EventDTO dto = new EventDTO(2L, "Over 2.5 goals",
                "Total goals over 2.5");

        Event expected = new Event(1L, 2L, "Over 2.5 goals",
                "Total goals over 2.5", null, null);

        Event actual = eventMapper.eventDtoToEntity(dto);
        actual.setEventId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void eventEntityToDtoTest() {
        Event entity = new Event(2L, 1L, "Over 2.5 goals",
                "Total goals over 2.5", 123L, 456L);

        EventDTO expected = new EventDTO(1L, "Over 2.5 goals",
                "Total goals over 2.5");

        EventDTO actual = eventMapper.eventEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void oddsDtoToEntityTest() {
        OddsDTO dto = new OddsDTO(2L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(1.5), true);

        Odds expected = new Odds(1L, 2L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(1.5), true, null, null);

        Odds actual = oddsMapper.oddsDtoToEntity(dto);
        actual.setOddsId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void oddsEntityToDtoTest() {
        Odds entity = new Odds(2L, 1L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(2.5), true, 1000L, 2000L);

        OddsDTO expected = new OddsDTO(1L, "Over", BigDecimal.valueOf(1.85),
                BigDecimal.valueOf(2.5), true);

        OddsDTO actual = oddsMapper.oddsEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetDtoToEntityTest() {
        PlayerBetDTO dto = new PlayerBetDTO("user123", 3L, BigDecimal.TEN, null);

        PlayerBet expected = new PlayerBet(1L, "user123", 3L, BigDecimal.TEN, null);

        PlayerBet actual = playerBetMapper.playerBetDtoToEntity(dto);
        actual.setBetId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetEntityToDtoTest() {
        PlayerBet entity = new PlayerBet(1L, "user123", 3L, BigDecimal.TEN, 123456789L);

        PlayerBetDTO expected = new PlayerBetDTO("user123", 3L, BigDecimal.TEN, null);

        PlayerBetDTO actual = playerBetMapper.playerBetEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetDtoToEntityWithSelectionTest() {
        PlayerBetDTO dto = new PlayerBetDTO("user123", 3L,
                BigDecimal.TEN, List.of(
                new PlayerBetSelectionDTO(3L, BigDecimal.ONE),
                new PlayerBetSelectionDTO(4L, BigDecimal.TWO)
        ));

        PlayerBet expected = new PlayerBet(1L, "user123",
                3L, BigDecimal.TEN, 123456789L);
        expected.setSelections(List.of(
                new PlayerBetSelection(11L, 1L, 3L,
                        BigDecimal.ONE, 123456789L),
                new PlayerBetSelection(11L, 1L, 4L,
                        BigDecimal.TWO, 123456789L)
        ));

        PlayerBet actual = playerBetMapper.playerBetDtoToEntity(dto);
        actual.setBetId(1L);
        actual.setSelections(actual.getSelections().stream()
                .map(playerBetSelection -> new PlayerBetSelection(
                        11L, 1L, playerBetSelection.getOddsId(),
                        playerBetSelection.getStake(), playerBetSelection.getCreationDate())).toList());

        assertEquals(expected, actual);
    }

    @Test
    void playerBetEntityToDtoWithSelectionsTest() {
        PlayerBet entity = new PlayerBet(1L, "user123",
                3L, BigDecimal.TEN, 123456789L);
        entity.setSelections(List.of(
                new PlayerBetSelection(11L, 1L, 3L,
                        BigDecimal.ONE, 123456789L),
                new PlayerBetSelection(11L, 1L, 4L,
                        BigDecimal.TWO, 123456789L)
        ));

        PlayerBetDTO expected = new PlayerBetDTO("user123", 3L,
                BigDecimal.TEN, List.of(
                new PlayerBetSelectionDTO(3L, BigDecimal.ONE),
                new PlayerBetSelectionDTO(4L, BigDecimal.TWO)
        ));

        PlayerBetDTO actual = playerBetMapper.playerBetEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetSelectionDtoToEntityTest() {
        PlayerBetSelectionDTO dto = new PlayerBetSelectionDTO(3L, BigDecimal.valueOf(100.50));

        PlayerBetSelection expected = new PlayerBetSelection(1L, 2L,
                3L, BigDecimal.valueOf(100.50), null);

        PlayerBetSelection actual = playerBetSelectionMapper.playerBetSelectionDtoToEntity(dto);
        actual.setSelectionId(1L);
        actual.setBetId(2L);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetSelectionEntityToDtoTest() {
        PlayerBetSelection entity = new PlayerBetSelection(1L, 2L,
                3L, BigDecimal.valueOf(100.50), 555555L);

        PlayerBetSelectionDTO expected = new PlayerBetSelectionDTO(3L, BigDecimal.valueOf(100.50));

        PlayerBetSelectionDTO actual = playerBetSelectionMapper.playerBetSelectionEntityToDto(entity);

        assertEquals(expected, actual);
    }
}
package com.pmolinav.predictionslib;

import com.pmolinav.predictionslib.dto.*;
import com.pmolinav.predictionslib.mapper.*;
import com.pmolinav.predictionslib.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappersTests {

    private final MatchMapper matchMapper = MatchMapper.INSTANCE;
    private final EventMapper eventMapper = EventMapper.INSTANCE;
    private final OddsMapper oddsMapper = OddsMapper.INSTANCE;
    private final PlayerBetMapper playerBetMapper = PlayerBetMapper.INSTANCE;
    private final PlayerBetSelectionMapper playerBetSelectionMapper = PlayerBetSelectionMapper.INSTANCE;

    @Test
    void matchDtoToEntityTest() {
        MatchDTO dto = new MatchDTO(1L, "PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, "SCHEDULED");

        Match expected = new Match(1L, "PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, "SCHEDULED", null, null);

        Match actual = matchMapper.matchDtoToEntity(dto);

        assertEquals(expected, actual);
    }

    @Test
    void matchEntityToDtoTest() {
        Match entity = new Match(1L, "PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, "SCHEDULED", null, null);

        MatchDTO expected = new MatchDTO(1L, "PREMIER", 2024, 3,
                "Team A", "Team B", 123456789L, "SCHEDULED");

        MatchDTO actual = matchMapper.matchEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void eventDtoToEntityTest() {
        EventDTO dto = new EventDTO(1L, 2L, "Over 2.5 goals",
                "Total goals over 2.5");
        Event expected = new Event(1L, 2L, "Over 2.5 goals",
                "Total goals over 2.5", null, null);

        Event actual = eventMapper.eventDtoToEntity(dto);

        assertEquals(expected, actual);
    }

    @Test
    void eventEntityToDtoTest() {
        Event entity = new Event(2L, 1L, "Over 2.5 goals",
                "Total goals over 2.5", 123L, 456L);

        EventDTO expected = new EventDTO(2L, 1L, "Over 2.5 goals",
                "Total goals over 2.5");

        EventDTO actual = eventMapper.eventEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void oddsDtoToEntityTest() {
        OddsDTO dto = new OddsDTO(1L, 2L, "Over",
                BigDecimal.valueOf(1.85), true);

        Odds expected = new Odds(1L, 2L, "Over",
                BigDecimal.valueOf(1.85), true, null, null);

        Odds actual = oddsMapper.oddsDtoToEntity(dto);

        assertEquals(expected, actual);
    }

    @Test
    void oddsEntityToDtoTest() {
        Odds entity = new Odds(2L, 1L, "Over",
                BigDecimal.valueOf(1.85), true, 1000L, 2000L);

        OddsDTO expected = new OddsDTO(2L, 1L, "Over",
                BigDecimal.valueOf(1.85), true);

        OddsDTO actual = oddsMapper.oddsEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetDtoToEntityTest() {
        PlayerBetDTO dto = new PlayerBetDTO(1L, "user123", 3L, null);

        PlayerBet expected = new PlayerBet(1L, "user123", 3L, null);

        PlayerBet actual = playerBetMapper.playerBetDtoToEntity(dto);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetEntityToDtoTest() {
        PlayerBet entity = new PlayerBet(1L, "user123", 3L, 123456789L);

        PlayerBetDTO expected = new PlayerBetDTO(1L, "user123", 3L, null);

        PlayerBetDTO actual = playerBetMapper.playerBetEntityToDto(entity);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetSelectionDtoToEntityTest() {
        PlayerBetSelectionDTO dto = new PlayerBetSelectionDTO(1L, 2L,
                3L, BigDecimal.valueOf(100.50));

        PlayerBetSelection expected = new PlayerBetSelection(1L, 2L,
                3L, BigDecimal.valueOf(100.50), null);

        PlayerBetSelection actual = playerBetSelectionMapper.playerBetSelectionDtoToEntity(dto);

        assertEquals(expected, actual);
    }

    @Test
    void playerBetSelectionEntityToDtoTest() {
        PlayerBetSelection entity = new PlayerBetSelection(1L, 2L,
                3L, BigDecimal.valueOf(100.50), 555555L);

        PlayerBetSelectionDTO expected = new PlayerBetSelectionDTO(1L, 2L,
                3L, BigDecimal.valueOf(100.50));

        PlayerBetSelectionDTO actual = playerBetSelectionMapper.playerBetSelectionEntityToDto(entity);

        assertEquals(expected, actual);
    }
}
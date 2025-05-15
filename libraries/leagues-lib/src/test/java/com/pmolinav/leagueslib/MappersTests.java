package com.pmolinav.leagueslib;

import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.mapper.LeagueCategoryMapper;
import com.pmolinav.leagueslib.mapper.LeagueMapper;
import com.pmolinav.leagueslib.mapper.LeaguePlayerMapper;
import com.pmolinav.leagueslib.mapper.MatchDayMapper;
import com.pmolinav.leagueslib.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappersTests {

    private final LeagueCategoryMapper leagueCategoryMapper = LeagueCategoryMapper.INSTANCE;
    private final MatchDayMapper matchDayMapper = MatchDayMapper.INSTANCE;
    private final LeagueMapper leagueMapper = LeagueMapper.INSTANCE;
    private final LeaguePlayerMapper leaguePlayerMapper = LeaguePlayerMapper.INSTANCE;

    @Test
    void leagueCategoryDTOToLeagueCategoryEntityTest() {
        LeagueCategoryDTO leagueCategoryDTO = new LeagueCategoryDTO("PREMIER", "Premier League",
                "English Premier League", "FOOTBALL", "UK", null, true);

        LeagueCategory expectedLeagueCategory = new LeagueCategory("PREMIER", "Premier League",
                "English Premier League", "FOOTBALL", "UK", null, true,
                1L, null);

        LeagueCategory leagueCategory = leagueCategoryMapper.leagueCategoryDtoToEntity(leagueCategoryDTO);

        assertEquals(expectedLeagueCategory, leagueCategory);
    }

    @Test
    void leagueCategoryEntityToLeagueCategoryDTOAdminTest() {
        LeagueCategory leagueCategory = new LeagueCategory("PREMIER", "Premier League",
                "English Premier League", "FOOTBALL", "UK", null, true,
                1L, null);

        LeagueCategoryDTO expectedLeagueCategoryDTO = new LeagueCategoryDTO("PREMIER", "Premier League",
                "English Premier League", "FOOTBALL", "UK", null, true);

        LeagueCategoryDTO leagueCategoryDTO = leagueCategoryMapper.leagueCategoryEntityToDto(leagueCategory);

        assertEquals(expectedLeagueCategoryDTO, leagueCategoryDTO);
    }

    @Test
    void matchDayDTOToMatchDayEntityTest() {
        MatchDayDTO matchDayDTO = new MatchDayDTO("PREMIER", 2025,
                3, 12345L, 123456789L);

        MatchDay expectedMatchDay = new MatchDay("PREMIER", 2025,
                3, 12345L, 123456789L);

        MatchDay matchDay = matchDayMapper.matchDayDtoToEntity(matchDayDTO);

        assertEquals(expectedMatchDay, matchDay);
    }

    @Test
    void matchDayEntityToMatchDayDTOAdminTest() {
        MatchDay matchDay = new MatchDay("PREMIER", 2013,
                26, 54321L, 7654321L);

        MatchDayDTO expectedMatchDayDTO = new MatchDayDTO("PREMIER", 2013,
                26, 54321L, 7654321L);

        MatchDayDTO matchDayDTO = matchDayMapper.matchDayEntityToDto(matchDay);

        assertEquals(expectedMatchDayDTO, matchDayDTO);
    }

    @Test
    void leagueDTOToLeagueEntityTest() {
        LeagueDTO leagueDTO = new LeagueDTO("New League", "League description",
                "PREMIER", true, null, LeagueStatus.ACTIVE, 10,
                null, false, "someUser");

        League expectedLeague = new League("New League", "League description",
                "PREMIER", true, null, LeagueStatus.ACTIVE, 10,
                null, false, "someUser", 123L, null);

        League league = leagueMapper.leagueDtoToEntity(leagueDTO);

        assertEquals(expectedLeague, league);
    }

    @Test
    void leagueEntityToLeagueDTOAdminTest() {
        League league = new League("New League", "League description",
                "PREMIER", true, null, LeagueStatus.ACTIVE, 10,
                null, false, "someUser", 123L, null);

        LeagueDTO expectedLeagueDTO = new LeagueDTO("New League", "League description",
                "PREMIER", true, null, LeagueStatus.ACTIVE, 10,
                null, false, "someUser");

        LeagueDTO leagueDTO = leagueMapper.leagueEntityToDto(league);

        assertEquals(expectedLeagueDTO, leagueDTO);
    }

    @Test
    void leaguePlayerDTOToLeaguePlayerEntityTest() {
        LeaguePlayerDTO leaguePlayerDTO = new LeaguePlayerDTO(1L, "someUser",
                333, PlayerStatus.ACTIVE);

        LeaguePlayer expectedLeaguePlayer = new LeaguePlayer(1L, "someUser",
                333, PlayerStatus.ACTIVE, 12345L);

        LeaguePlayer leaguePlayer = leaguePlayerMapper.leaguePlayerDtoToEntity(leaguePlayerDTO);

        assertEquals(expectedLeaguePlayer, leaguePlayer);
    }

    @Test
    void leaguePlayerEntityToLeaguePlayerDTOAdminTest() {
        LeaguePlayer leaguePlayer = new LeaguePlayer(1L, "someUser",
                333, PlayerStatus.ACTIVE, 12345L);

        LeaguePlayerDTO expectedLeaguePlayerDTO = new LeaguePlayerDTO(1L, "someUser",
                333, PlayerStatus.ACTIVE);

        LeaguePlayerDTO leaguePlayerDTO = leaguePlayerMapper.leaguePlayerEntityToDto(leaguePlayer);

        assertEquals(expectedLeaguePlayerDTO, leaguePlayerDTO);
    }
}

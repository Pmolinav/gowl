package com.pmolinav.leagueslib;

import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.mapper.LeagueCategoryMapper;
import com.pmolinav.leagueslib.mapper.MatchDayMapper;
import com.pmolinav.leagueslib.model.LeagueCategory;
import com.pmolinav.leagueslib.model.MatchDay;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappersTests {

    private final LeagueCategoryMapper leagueCategoryMapper = LeagueCategoryMapper.INSTANCE;
    private final MatchDayMapper matchDayMapper = MatchDayMapper.INSTANCE;

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

}

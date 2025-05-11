package com.pmolinav.leagueslib;

import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.mapper.LeagueCategoryMapper;
import com.pmolinav.leagueslib.model.LeagueCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappersTests {

    private final LeagueCategoryMapper leagueCategoryMapper = LeagueCategoryMapper.INSTANCE;

    @Test
    void userDTOToUserEntityTest() {
        LeagueCategoryDTO leagueCategoryDTO = new LeagueCategoryDTO("PREMIER", "Premier League",
                "English Premier League", "FOOTBALL", "UK", null, true);

        LeagueCategory expectedLeagueCategory = new LeagueCategory("PREMIER", "Premier League",
                "English Premier League", "FOOTBALL", "UK", null, true,
                1L, null);

        LeagueCategory leagueCategory = leagueCategoryMapper.leagueCategoryDtoToEntity(leagueCategoryDTO);

        assertEquals(expectedLeagueCategory, leagueCategory);
    }

    @Test
    void userEntityToUserDTOAdminTest() {
        LeagueCategory leagueCategory = new LeagueCategory("PREMIER", "Premier League",
                "English Premier League", "FOOTBALL", "UK", null, true,
                1L, null);

        LeagueCategoryDTO expectedLeagueCategoryDTO = new LeagueCategoryDTO("PREMIER", "Premier League",
                "English Premier League", "FOOTBALL", "UK", null, true);

        LeagueCategoryDTO leagueCategoryDTO = leagueCategoryMapper.leagueCategoryEntityToDto(leagueCategory);

        assertEquals(expectedLeagueCategoryDTO, leagueCategoryDTO);
    }

}

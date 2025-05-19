package com.pmolinav.leagueslib.mapper;

import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import com.pmolinav.leagueslib.model.LeaguePlayerPoints;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LeaguePlayerPointsMapper {

    LeaguePlayerPointsMapper INSTANCE = Mappers.getMapper(LeaguePlayerPointsMapper.class);

    LeaguePlayerPoints leaguePlayerPointsDtoToEntity(LeaguePlayerPointsDTO dto);

    LeaguePlayerPointsDTO leaguePlayerPointsEntityToDto(LeaguePlayerPoints entity);

    void updateEntityFromDto(LeaguePlayerPointsDTO dto, @MappingTarget LeaguePlayerPoints entity);
}


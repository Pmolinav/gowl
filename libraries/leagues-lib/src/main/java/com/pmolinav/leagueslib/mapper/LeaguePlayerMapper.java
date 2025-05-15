package com.pmolinav.leagueslib.mapper;

import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeaguePlayer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LeaguePlayerMapper {

    LeaguePlayerMapper INSTANCE = Mappers.getMapper(LeaguePlayerMapper.class);

    @Mapping(target = "joinDate", expression = "java(System.currentTimeMillis())")
    LeaguePlayer leaguePlayerDtoToEntity(LeaguePlayerDTO dto);

    LeaguePlayerDTO leaguePlayerEntityToDto(LeaguePlayer entity);

    void updateEntityFromDto(LeaguePlayerDTO dto, @MappingTarget LeaguePlayer entity);
}


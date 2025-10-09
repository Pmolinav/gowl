package com.pmolinav.leagueslib.mapper;

import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.model.League;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface LeagueMapper {

    LeagueMapper INSTANCE = Mappers.getMapper(LeagueMapper.class);

    @Mapping(target = "creationDate", expression = "java(System.currentTimeMillis())")
    @Mapping(target = "modificationDate", expression = "java(System.currentTimeMillis())")
    League leagueDtoToEntity(LeagueDTO dto);

    LeagueDTO leagueEntityToDto(League entity);

    void updateEntityFromDto(LeagueDTO dto, @MappingTarget League entity);
}


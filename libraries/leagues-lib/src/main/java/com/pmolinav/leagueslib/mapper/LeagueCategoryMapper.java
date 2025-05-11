package com.pmolinav.leagueslib.mapper;

import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.model.LeagueCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LeagueCategoryMapper {

    LeagueCategoryMapper INSTANCE = Mappers.getMapper(LeagueCategoryMapper.class);

    @Mapping(target = "creationDate", expression = "java(System.currentTimeMillis())")
    @Mapping(target = "modificationDate", expression = "java(System.currentTimeMillis())")
    LeagueCategory leagueCategoryDtoToEntity(LeagueCategoryDTO dto);

    LeagueCategoryDTO leagueCategoryEntityToDto(LeagueCategory entity);

    void updateEntityFromDto(LeagueCategoryDTO dto, @MappingTarget LeagueCategory entity);
}


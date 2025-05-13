package com.pmolinav.leagueslib.mapper;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.model.MatchDay;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MatchDayMapper {

    MatchDayMapper INSTANCE = Mappers.getMapper(MatchDayMapper.class);

    MatchDay matchDayDtoToEntity(MatchDayDTO dto);

    MatchDayDTO matchDayEntityToDto(MatchDay entity);

    void updateEntityFromDto(MatchDayDTO dto, @MappingTarget MatchDay entity);
}


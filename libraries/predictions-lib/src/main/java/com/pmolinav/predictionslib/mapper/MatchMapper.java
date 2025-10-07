package com.pmolinav.predictionslib.mapper;

import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.dto.SimpleMatchDTO;
import com.pmolinav.predictionslib.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(target = "creationDate", expression = "java(System.currentTimeMillis())")
    @Mapping(target = "modificationDate", expression = "java(System.currentTimeMillis())")
    Match matchDtoToEntity(MatchDTO matchDTO);

    MatchDTO matchEntityToDto(Match match);

    SimpleMatchDTO matchEntityToSimpleDto(Match match);

    SimpleMatchDTO matchDTOToSimpleDto(MatchDTO matchDTO);

    void updateEntityFromDto(MatchDTO dto, @MappingTarget Match entity);
}


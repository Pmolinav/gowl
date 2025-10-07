package com.pmolinav.predictionslib.mapper;

import com.pmolinav.predictionslib.dto.OddsDTO;
import com.pmolinav.predictionslib.dto.SimpleOddsDTO;
import com.pmolinav.predictionslib.model.Odds;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OddsMapper {

    OddsMapper INSTANCE = Mappers.getMapper(OddsMapper.class);

    @Mapping(target = "creationDate", expression = "java(System.currentTimeMillis())")
    @Mapping(target = "modificationDate", expression = "java(System.currentTimeMillis())")
    Odds oddsDtoToEntity(OddsDTO dto);

    OddsDTO oddsEntityToDto(Odds entity);

    SimpleOddsDTO oddsEntityToSimpleDto(Odds entity);

    SimpleOddsDTO oddsDTOToSimpleDto(OddsDTO oddsDTO);

    void updateEntityFromDto(OddsDTO dto, @MappingTarget Odds entity);
}


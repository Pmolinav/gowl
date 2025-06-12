package com.pmolinav.predictionslib.mapper;

import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.predictionslib.model.PlayerBet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PlayerBetMapper {

    PlayerBetMapper INSTANCE = Mappers.getMapper(PlayerBetMapper.class);

    @Mapping(target = "creationDate", expression = "java(System.currentTimeMillis())")
    PlayerBet playerBetDtoToEntity(PlayerBetDTO dto);

    PlayerBetDTO playerBetEntityToDto(PlayerBet entity);

    void updateEntityFromDto(PlayerBetDTO dto, @MappingTarget PlayerBet entity);
}


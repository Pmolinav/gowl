package com.pmolinav.predictionslib.mapper;

import com.pmolinav.predictionslib.dto.PlayerBetSelectionByUsernameDTO;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
import com.pmolinav.predictionslib.model.PlayerBetSelection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PlayerBetSelectionMapper {

    PlayerBetSelectionMapper INSTANCE = Mappers.getMapper(PlayerBetSelectionMapper.class);

    @Mapping(target = "creationDate", expression = "java(System.currentTimeMillis())")
    PlayerBetSelection playerBetSelectionDtoToEntity(PlayerBetSelectionDTO dto);

    PlayerBetSelectionDTO playerBetSelectionEntityToDto(PlayerBetSelection entity);

    @Mapping(target = "odds", source = "odds")
    PlayerBetSelectionByUsernameDTO playerBetEntityToByUsernameDtoList(PlayerBetSelection selection);

    void updateEntityFromDto(PlayerBetSelectionDTO dto, @MappingTarget PlayerBetSelection entity);
}


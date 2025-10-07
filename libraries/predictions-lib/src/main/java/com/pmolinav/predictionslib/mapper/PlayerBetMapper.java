package com.pmolinav.predictionslib.mapper;

import com.pmolinav.predictionslib.dto.PlayerBetByUsernameDTO;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionByUsernameDTO;
import com.pmolinav.predictionslib.model.PlayerBet;
import com.pmolinav.predictionslib.model.PlayerBetSelection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface PlayerBetMapper {

    PlayerBetMapper INSTANCE = Mappers.getMapper(PlayerBetMapper.class);

    @Mapping(target = "creationDate",
            expression = "java(dto.getCreationDate() == null ? System.currentTimeMillis() : dto.getCreationDate())")
    @Mapping(target = "selections", source = "selections")
    PlayerBet playerBetDtoToEntity(PlayerBetDTO dto);

    @Mapping(target = "selections", source = "selections")
    PlayerBetDTO playerBetEntityToDto(PlayerBet entity);

    @Mapping(target = "match", source = "match")
    @Mapping(target = "selections", source = "selections")
    PlayerBetByUsernameDTO playerBetEntityToByUsernameDto(PlayerBet entity);

    List<PlayerBetByUsernameDTO> playerBetEntityToByUsernameDtoList(List<PlayerBet> entities);

    void updateEntityFromDto(PlayerBetDTO dto, @MappingTarget PlayerBet entity);
}


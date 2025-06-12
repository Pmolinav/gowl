package com.pmolinav.predictionslib.mapper;

import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "creationDate", expression = "java(System.currentTimeMillis())")
    @Mapping(target = "modificationDate", expression = "java(System.currentTimeMillis())")
    Event eventDtoToEntity(EventDTO dto);

    EventDTO eventEntityToDto(Event entity);

    void updateEntityFromDto(EventDTO dto, @MappingTarget Event entity);
}


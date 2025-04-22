package net.pmolinav.bookingslib.mapper;

import net.pmolinav.bookingslib.dto.ActivityDTO;
import net.pmolinav.bookingslib.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ActivityMapper {

    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
    @Mapping(target = "modificationDate", expression = "java(new java.util.Date())")
    Activity activityDTOToActivityEntity(ActivityDTO activityDTO);

    ActivityDTO activityEntityTOActivityDTO(Activity activity);

}

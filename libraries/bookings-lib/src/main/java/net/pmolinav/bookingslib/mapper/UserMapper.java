package net.pmolinav.bookingslib.mapper;

import net.pmolinav.bookingslib.dto.UserDTO;
import net.pmolinav.bookingslib.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
    @Mapping(target = "modificationDate", expression = "java(new java.util.Date())")
    User userDTOToUserEntity(UserDTO userDTO);

    UserDTO userEntityToUserDTO(User user);

}

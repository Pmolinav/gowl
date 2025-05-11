package com.pmolinav.userslib.mapper;

import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.Role;
import com.pmolinav.userslib.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "creationDate", expression = "java(System.currentTimeMillis())")
    @Mapping(target = "modificationDate", expression = "java(System.currentTimeMillis())")
    User userDTOToUserEntity(UserDTO userDTO);

    @Mapping(target = "admin", expression = "java(hasAdminRole(user.getRoles()))")
    UserDTO userEntityToUserDTO(User user);

    default boolean hasAdminRole(List<Role> roles) {
        return roles != null && roles.stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
    }
}

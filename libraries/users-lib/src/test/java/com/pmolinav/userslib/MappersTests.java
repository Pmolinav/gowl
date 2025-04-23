package com.pmolinav.userslib;

import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.mapper.UserMapper;
import com.pmolinav.userslib.model.Role;
import com.pmolinav.userslib.model.User;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappersTests {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    void userDTOToUserEntityTest() {
        UserDTO userDTO = new UserDTO("someUser", "somePassword", "someName",
                "some@email.com", false);

        User expectedUser = new User(null, "someUser", "somePassword", "someName",
                "some@email.com", new Date(1L), null, null);

        User user = userMapper.userDTOToUserEntity(userDTO);

        assertEquals(expectedUser, user);
    }

    @Test
    void userEntityToUserDTOAdminTest() {
        User user = new User(1L, "someUser", "somePassword", "someName",
                "some@email.com", new Date(1L), null,
                List.of(new Role(1L, "ROLE_ADMIN")));

        UserDTO expectedUserDTO = new UserDTO("someUser", "somePassword", "someName",
                "some@email.com", true);

        UserDTO userDTO = userMapper.userEntityToUserDTO(user);

        assertEquals(expectedUserDTO, userDTO);
    }
    @Test
    void userEntityToUserDTOTest() {
        User user = new User(1L, "someUser", "somePassword", "someName",
                "some@email.com", new Date(1L), null,
                List.of(new Role(1L, "ROLE_USER"),new Role(2L, "ROLE_OTHER")));

        UserDTO expectedUserDTO = new UserDTO("someUser", "somePassword", "someName",
                "some@email.com", false);

        UserDTO userDTO = userMapper.userEntityToUserDTO(user);

        assertEquals(expectedUserDTO, userDTO);
    }

}

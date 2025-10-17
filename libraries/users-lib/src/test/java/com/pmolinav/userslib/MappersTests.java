package com.pmolinav.userslib;

import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.dto.UserPublicDTO;
import com.pmolinav.userslib.mapper.UserMapper;
import com.pmolinav.userslib.model.Role;
import com.pmolinav.userslib.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappersTests {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    void userDTOToUserEntityTest() {
        UserDTO userDTO = new UserDTO("someUser", "somePassword", "someName",
                "some@email.com", LocalDate.of(1997, 6, 9), false);

        User expectedUser = new User(null, "someUser", "somePassword", "someName",
                "some@email.com", LocalDate.of(1997, 6, 9),
                1L, null, null);

        User user = userMapper.userDTOToUserEntity(userDTO);

        assertEquals(expectedUser, user);
    }

    @Test
    void userEntityToUserDTOAdminTest() {
        User user = new User(1L, "someUser", "somePassword", "someName",
                "some@email.com", LocalDate.of(1997, 6, 9), 1L, null,
                List.of(new Role(1L, "ROLE_ADMIN")));

        UserDTO expectedUserDTO = new UserDTO("someUser", "somePassword", "someName",
                "some@email.com", LocalDate.of(1997, 6, 9), true);

        UserDTO userDTO = userMapper.userEntityToUserDTO(user);

        assertEquals(expectedUserDTO, userDTO);
    }

    @Test
    void userEntityToUserDTOTest() {
        User user = new User(1L, "someUser", "somePassword", "someName",
                "some@email.com", LocalDate.of(1997, 6, 9), 1L, null,
                List.of(new Role(1L, "ROLE_USER"), new Role(2L, "ROLE_OTHER")));

        UserDTO expectedUserDTO = new UserDTO("someUser", "somePassword", "someName",
                "some@email.com", LocalDate.of(1997, 6, 9), false);

        UserDTO userDTO = userMapper.userEntityToUserDTO(user);

        assertEquals(expectedUserDTO, userDTO);
    }

    @Test
    void userDTOToUserPublicDTOTest() {
        UserDTO userDTO = new UserDTO("someUser", "somePassword",
                "someName", "some@email.com", LocalDate.of(1997, 6, 9), false);

        UserPublicDTO expectedUserPublicDTO = new UserPublicDTO("someUser", "somePassword",
                "someName", "some@email.com", LocalDate.of(1997, 6, 9));

        UserPublicDTO userPublicDTO = userMapper.userDTOToUserPublicDTO(userDTO);

        assertEquals(expectedUserPublicDTO, userPublicDTO);
    }

    @Test
    void userPublicDTOToUserDTOTest() {
        UserDTO expectedUserDTO = new UserDTO("someUser", "somePassword",
                "someName", "some@email.com", LocalDate.of(1997, 6, 9), false);

        UserPublicDTO userPublicDTO = new UserPublicDTO("someUser", "somePassword",
                "someName", "some@email.com", LocalDate.of(1997, 6, 9));

        UserDTO userDTO = userMapper.userPublicDTOToUserDTO(userPublicDTO);

        assertEquals(expectedUserDTO, userDTO);
    }
}

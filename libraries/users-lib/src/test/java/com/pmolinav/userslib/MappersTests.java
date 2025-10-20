package com.pmolinav.userslib;

import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.dto.UserPublicDTO;
import com.pmolinav.userslib.dto.UserTokenDTO;
import com.pmolinav.userslib.mapper.UserMapper;
import com.pmolinav.userslib.model.Role;
import com.pmolinav.userslib.model.Token;
import com.pmolinav.userslib.model.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Test
    void tokenEntityToDTOTest() {
        LocalDateTime now = LocalDateTime.now();

        Token token = new Token(1L, "someUser", "someToken", "Mozilla Agent",
                "192.168.0.1", 12345L, 12345L, now);

        UserTokenDTO expectedUserTokenDTO = new UserTokenDTO("someUser", "someToken", "Mozilla Agent",
                "192.168.0.1", now);

        UserTokenDTO userTokenDTO = userMapper.tokenEntityToDTO(token);

        assertEquals(expectedUserTokenDTO, userTokenDTO);
    }

    @Test
    void userTokenDTOToEntityTest() {
        LocalDateTime now = LocalDateTime.ofInstant(Instant.ofEpochMilli(123456L), ZoneId.systemDefault());

        UserTokenDTO userTokenDTO = new UserTokenDTO("someUser", "someToken", "Mozilla Agent",
                "192.168.0.1", now);

        Token expectedToken = new Token(1L, "someUser", "someToken", "Mozilla Agent",
                "192.168.0.1", 12345L, 12345L, now);

        Token token = userMapper.userTokenDTOToEntity(userTokenDTO);
        token.setId(1L);

        assertEquals(expectedToken, token);
    }
}

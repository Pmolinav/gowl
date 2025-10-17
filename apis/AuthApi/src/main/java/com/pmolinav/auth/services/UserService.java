package com.pmolinav.auth.services;

import com.pmolinav.auth.clients.UserClient;
import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.exceptions.InternalServerErrorException;
import com.pmolinav.auth.exceptions.NotFoundException;
import com.pmolinav.userslib.dto.UpdatePasswordDTO;
import com.pmolinav.userslib.dto.UpdateUserDTO;
import com.pmolinav.userslib.dto.UserPublicDTO;
import com.pmolinav.userslib.mapper.UserMapper;
import com.pmolinav.userslib.model.User;
import feign.FeignException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserClient userClient;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserClient userClient, UserMapper userMapper) {
        this.userClient = userClient;
        this.userMapper = userMapper;
    }

    public Long createUser(UserPublicDTO userPublicDTO) {
        try {
            // Mapped to always create non-admin users from exposed API.
            return userClient.createUser(this.userMapper.userPublicDTOToUserDTO(userPublicDTO));
        } catch (FeignException e) {
            logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
            throw new CustomStatusException(e.getMessage(), e.status());
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public User findUserById(long id) {
        try {
            return userClient.findUserById(id);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("User with id {} not found.", id, e);
                throw new NotFoundException("User " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public User findUserByUsername(String username) {
        try {
            return userClient.findUserByUsername(username);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("User with username {} not found.", username, e);
                throw new NotFoundException("User " + username + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }


    public void updateUserByUsername(String username, UpdateUserDTO updateUserDTO) {
        try {
            userClient.updateUserByUsername(username, updateUserDTO);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("User with username {} not found.", username, e);
                throw new NotFoundException("User " + username + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void updateUserPasswordByUsername(String username, UpdatePasswordDTO updatePasswordDTO) {
        try {
            userClient.updateUserPasswordByUsername(username, updatePasswordDTO);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("User with username {} not found.", username, e);
                throw new NotFoundException("User " + username + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteUser(Long id) {
        try {
            userClient.deleteUser(id);
        } catch (FeignException e) {
            if (e instanceof RetryableException) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("User with id {} not found.", id, e);
                throw new NotFoundException("User " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
    //TODO: Remove user (not DELETE)
}

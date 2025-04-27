package com.pmolinav.users.services;

import com.pmolinav.users.exceptions.InternalServerErrorException;
import com.pmolinav.users.exceptions.NotFoundException;
import com.pmolinav.users.repositories.UserRepository;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.mapper.UserMapper;
import com.pmolinav.userslib.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@EnableAsync
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private final UserMapper userMapper;
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        List<User> usersList;
        try {
            usersList = userRepository.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while searching all users in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(usersList)) {
            logger.warn("No users were found in repository.");
            throw new NotFoundException("No users found in repository.");
        } else {
            return usersList;
        }
    }

    @Transactional
    public User createUser(UserDTO userDTO) {
        try {
            User user = userMapper.userDTOToUserEntity(userDTO);
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Unexpected error while creating new user in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public User findById(long id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist.", id)));
        } catch (NotFoundException e) {
            logger.error("User with id {} was not found.", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while searching user with id {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        try {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException(String.format("User with username %s does not exist.", username)));
        } catch (NotFoundException e) {
            logger.error("User with username {} was not found.", username, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while searching user with username {} in repository.", username, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist.", id)));

            userRepository.delete(user);
        } catch (NotFoundException e) {
            logger.error("User with id {} was not found.", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing user with id {} in repository.", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

//    @Async
//    public void storeInKafka(ChangeType changeType, Long userId, User user) {
//        try {
//            messageProducer.sendMessage(this.KAFKA_TOPIC, new History(
//                    new Date(),
//                    changeType,
//                    "User",
//                    String.valueOf(userId),
//                    user == null ? null : new ObjectMapper().writeValueAsString(user), // TODO: USE JSON PATCH.
//                    "Admin" // TODO: createUser is not implemented yet.
//            ));
//        } catch (Exception e) {
//            logger.warn("Kafka operation {} with name {} and user {} need to be reviewed", changeType, userId, user);
//        }
//    }
}

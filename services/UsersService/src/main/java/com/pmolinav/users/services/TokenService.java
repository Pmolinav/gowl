package com.pmolinav.users.services;

import com.pmolinav.users.exceptions.InternalServerErrorException;
import com.pmolinav.users.exceptions.UnauthorizedException;
import com.pmolinav.users.repositories.TokenRepository;
import com.pmolinav.userslib.dto.LogoutDTO;
import com.pmolinav.userslib.dto.UserTokenDTO;
import com.pmolinav.userslib.mapper.UserMapper;
import com.pmolinav.userslib.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@EnableAsync
@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public TokenService(TokenRepository tokenRepository, UserMapper userMapper) {
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public boolean existsTokenForUsername(String username, String refreshToken) {
        try {
            Token token = tokenRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

            if (!token.getUsername().equals(username)) {
                throw new UnauthorizedException("Token does not belong to user");
            }
            return true;
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while checking if token exists by username {} in repository.", username, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public Token saveToken(UserTokenDTO userTokenDTO) {
        try {
            Token token = userMapper.userTokenDTOToEntity(userTokenDTO);
            return tokenRepository.save(token);
        } catch (Exception e) {
            logger.error("Unexpected error while creating new token in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void invalidateToken(LogoutDTO logoutDTO) {
        try {
            tokenRepository.deleteByUsernameAndRefreshToken(logoutDTO.getUsername(), logoutDTO.getRefreshToken());
        } catch (Exception e) {
            logger.error("Unexpected error while creating new token in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void invalidateAllTokens(String username) {
        try {
            tokenRepository.deleteByUsername(username);
        } catch (Exception e) {
            logger.error("Unexpected error while creating new token in repository.", e);
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

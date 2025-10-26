package com.pmolinav.prediction.auth.interceptors;

import com.pmolinav.prediction.auth.AuthUtils;
import com.pmolinav.prediction.exceptions.CustomStatusException;
import com.pmolinav.prediction.services.PlayerBetService;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PlayerBetAccessInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBetAccessInterceptor.class);

    private final PlayerBetService playerBetService;
    private final AuthUtils authUtils;

    public PlayerBetAccessInterceptor(PlayerBetService playerBetService, AuthUtils authUtils) {
        this.playerBetService = playerBetService;
        this.authUtils = authUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!"DELETE".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String username = authUtils.getAuthenticatedUsername();
        String path = request.getRequestURI();

        try {
            // DELETE /{id}
            String idPart = path.substring(path.lastIndexOf("/") + 1);
            long id = Long.parseLong(idPart);

            PlayerBetDTO playerBetDTO = playerBetService.findById(id);
            if (!playerBetDTO.getUsername().equals(username)) {
                logger.error("Requested user does not have permissions for path {}.", path);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to delete this player bet.");
                return false;
            }

            return true;
        } catch (CustomStatusException e) {
            logger.error("Unexpected error occurred with status {}.", e.getStatusCode().value(), e);
            response.sendError(e.getStatusCode().value(), e.getMessage());
            return false;
        }
    }
}

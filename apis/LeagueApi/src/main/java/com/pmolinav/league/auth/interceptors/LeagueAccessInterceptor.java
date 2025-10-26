package com.pmolinav.league.auth.interceptors;

import com.pmolinav.league.auth.AuthUtils;
import com.pmolinav.league.exceptions.CustomStatusException;
import com.pmolinav.league.services.LeaguesService;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LeagueAccessInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LeagueAccessInterceptor.class);

    private final LeaguesService leaguesService;
    private final AuthUtils authUtils;

    public LeagueAccessInterceptor(LeaguesService leaguesService, AuthUtils authUtils) {
        this.leaguesService = leaguesService;
        this.authUtils = authUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String username = authUtils.getAuthenticatedUsername();
        String path = request.getRequestURI();

        boolean allowed = false;
        try {
            if (path.matches(".*/close/[^/]+$")) {
                // PUT /close/{id}
                String idPart = path.substring(path.lastIndexOf("/") + 1);
                long leagueId = Long.parseLong(idPart);

                LeagueDTO leagueDTO = leaguesService.findLeagueById(leagueId);
                if (leagueDTO.getOwnerUsername().equals(username) &&
                        leagueDTO.getLeaguePlayers().stream().anyMatch(
                                leaguePlayerDTO -> leaguePlayerDTO.getUsername().equals(username))) {
                    allowed = true;
                }
            } else if (path.matches(".*/close/names/[^/]+$")) {
                // PUT /close/names/{name}
                String name = path.substring(path.lastIndexOf("/") + 1);

                LeagueDTO leagueDTO = leaguesService.findLeagueByName(name);
                if (leagueDTO.getOwnerUsername().equals(username) &&
                        leagueDTO.getLeaguePlayers().stream().anyMatch(
                                leaguePlayerDTO -> leaguePlayerDTO.getUsername().equals(username))) {
                    allowed = true;
                }
            } else if (path.matches(".*/leagues/[^/]+$")) {
                // GET /leagues/{id}
                String idPart = path.substring(path.lastIndexOf("/") + 1);
                long leagueId = Long.parseLong(idPart);

                LeagueDTO leagueDTO = leaguesService.findLeagueById(leagueId);
                if (leagueDTO.getLeaguePlayers().stream().anyMatch(
                        leaguePlayerDTO -> leaguePlayerDTO.getUsername().equals(username))) {
                    allowed = true;
                }
            } else if (path.matches(".*/leagues/names/[^/]+$")) {
                // GET /leagues/names/{name}
                String name = path.substring(path.lastIndexOf("/") + 1);

                LeagueDTO leagueDTO = leaguesService.findLeagueByName(name);
                if (leagueDTO.getLeaguePlayers().stream().anyMatch(
                        leaguePlayerDTO -> leaguePlayerDTO.getUsername().equals(username))) {
                    allowed = true;
                }
            } else if (path.matches(".*/leagues/username/[^/]+$")) {
                // GET /leagues/username/{username}
                String requestedUsername = path.substring(path.lastIndexOf("/") + 1);

                // Only allowed if token's username matches with username from the path.
                if (requestedUsername.equals(username)) {
                    allowed = true;
                }
            }
        } catch (CustomStatusException e) {
            logger.error("Unexpected error occurred with status {}.", e.getStatusCode().value(), e);
            response.sendError(e.getStatusCode().value(), e.getMessage());
            return false;
        }
        if (!allowed) {
            logger.error("Requested user does not have permissions for path {}.", path);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not belong to this league");
            return false;
        }

        return true;
    }
}

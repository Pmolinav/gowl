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
public class LeaguePlayerPointsAccessInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LeaguePlayerPointsAccessInterceptor.class);

    private final AuthUtils authUtils;
    private final LeaguesService leaguesService;

    public LeaguePlayerPointsAccessInterceptor(AuthUtils authUtils, LeaguesService leaguesService) {
        this.authUtils = authUtils;
        this.leaguesService = leaguesService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String usernameFromJwt = authUtils.getAuthenticatedUsername();
        String path = request.getRequestURI();

        try {
            // GET /league-player-points/leagues/{id}/players/{username}
            if ("GET".equalsIgnoreCase(request.getMethod())
                    && path.matches(".*/league-player-points/leagues/\\d+/players/[^/]+$")) {

                String leagueIdPart = path.split("/")[3];
                long leagueId = Long.parseLong(leagueIdPart);

                if (!isUserInLeague(usernameFromJwt, leagueId)) {
                    logger.error("Requested user does not have permissions for path {}.", path);
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not in league");
                    return false;
                }
            }

        } catch (CustomStatusException e) {
            logger.error("Unexpected error occurred with status {}.", e.getStatusCode().value(), e);
            response.sendError(e.getStatusCode().value(), e.getMessage());
            return false;
        }

        return true;
    }

    private boolean isUserInLeague(String username, long leagueId) {
        LeagueDTO league = leaguesService.findLeagueById(leagueId);
        return league.getLeaguePlayers().stream()
                .anyMatch(p -> p.getUsername().equals(username));
    }
}


package com.pmolinav.league.auth.interceptors;

import com.pmolinav.league.auth.AuthUtils;
import com.pmolinav.league.services.LeaguesService;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LeaguePlayersAccessInterceptor implements HandlerInterceptor {

    private final AuthUtils authUtils;
    private final LeaguesService leaguesService;

    public LeaguePlayersAccessInterceptor(AuthUtils authUtils, LeaguesService leaguesService) {
        this.authUtils = authUtils;
        this.leaguesService = leaguesService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String usernameFromJwt = authUtils.getAuthenticatedUsername();
        String path = request.getRequestURI();

        try {
            if ("GET".equalsIgnoreCase(request.getMethod())) {

                // GET /league-players/leagues/{id}/players/{username}
                if (path.matches(".*/league-players/leagues/\\d+/players/[^/]+$")) {
                    String usernamePath = path.substring(path.lastIndexOf("/") + 1);
                    String leagueIdPart = path.split("/")[3];
                    long leagueId = Long.parseLong(leagueIdPart);

                    if (!isUserInLeague(usernameFromJwt, leagueId)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not in league");
                        return false;
                    }

                    // GET /league-players/leagues/{id}
                } else if (path.matches(".*/league-players/leagues/\\d+$")) {
                    String idPart = path.substring(path.lastIndexOf("/") + 1);
                    long leagueId = Long.parseLong(idPart);

                    if (!isUserInLeague(usernameFromJwt, leagueId)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not in league");
                        return false;
                    }

                    // GET /league-players/players/{username}/leagues
                } else if (path.matches(".*/league-players/players/[^/]+/leagues$")) {
                    String usernamePath = path.split("/")[3];
                    if (!usernamePath.equals(usernameFromJwt)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot access leagues of another user");
                        return false;
                    }
                }

            } else if ("POST".equalsIgnoreCase(request.getMethod())) {
                return true;
            } else if ("DELETE".equalsIgnoreCase(request.getMethod())) {

                // DELETE /league-players/leagues/{id}/players/{username}
                if (path.matches(".*/league-players/leagues/\\d+/players/[^/]+$")) {
                    String usernamePath = path.substring(path.lastIndexOf("/") + 1);
                    if (!usernamePath.equals(usernameFromJwt)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can only remove yourself from the league");
                        return false;
                    }
                }
            }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Interceptor error: " + e.getMessage());
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


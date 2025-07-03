package com.pmolinav.auth.auth.interceptors;

import com.pmolinav.auth.auth.AuthUtils;
import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.services.UserService;
import com.pmolinav.userslib.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserAccessInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final AuthUtils authUtils;

    public UserAccessInterceptor(UserService userService, AuthUtils authUtils) {
        this.userService = userService;
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
            if (path.matches(".*/users/[^/]+$")) {
                // GET or DELETE /users/{id}
                String idPart = path.substring(path.lastIndexOf("/") + 1);
                long userId = Long.parseLong(idPart);

                User user = userService.findUserById(userId);
                if (user.getUsername().equals(username)) {
                    allowed = true;
                }
            }
        } catch (CustomStatusException e) {
            response.sendError(e.getStatusCode().value(), e.getMessage());
            return false;
        }
        if (!allowed) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permissions for this user.");
            return false;
        }

        return true;
    }
}

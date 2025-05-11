package com.pmolinav.leagues.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalNetworkFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String remoteAddress = request.getRemoteAddr();

        if (isInternalDockerNetwork(remoteAddress)) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied for external IP " + remoteAddress);
        }
    }

    private boolean isInternalDockerNetwork(String ip) {
        // Docker bridge network usually starts with 172.16.x.x to 172.31.x.x (RFC1918)
        // Localhost is also added for testing purposes.
        return ip.startsWith("172.") || ip.equals("127.0.0.1");
    }
}


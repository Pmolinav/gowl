package com.pmolinav.league.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.auth.utils.TokenUtils;
import com.pmolinav.league.auth.TokenConfig;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class JwtValidationFilter extends BasicAuthenticationFilter {

    private final TokenConfig tokenConfig;

    public JwtValidationFilter(AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        super(authenticationManager);
        this.tokenConfig = tokenConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = header.replace("Bearer ", "");
            UsernamePasswordAuthenticationToken authentication = new TokenUtils(
                    this.tokenConfig.getSecret(),
                    this.tokenConfig.getValiditySeconds(),
                    this.tokenConfig.getRefreshValiditySeconds()).getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (JwtException e) {
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "Invalid JWT!");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType("application/json");
        }
    }
}

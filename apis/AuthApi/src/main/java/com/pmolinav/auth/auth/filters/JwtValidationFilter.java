package com.pmolinav.auth.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.auth.auth.TokenConfig;
import com.pmolinav.auth.utils.TokenUtils;
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

import static com.pmolinav.auth.utils.TokenUtils.HEADER_AUTHORIZATION;
import static com.pmolinav.auth.utils.TokenUtils.PREFIX_TOKEN;

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

        String header = request.getHeader(HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = header.replace(PREFIX_TOKEN, "");
            UsernamePasswordAuthenticationToken authentication = new TokenUtils(
                    this.tokenConfig.getSecret(),
                    this.tokenConfig.getValiditySeconds()).getAuthentication(token);

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

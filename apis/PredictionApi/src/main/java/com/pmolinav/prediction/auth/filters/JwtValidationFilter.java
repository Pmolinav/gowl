package com.pmolinav.prediction.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.auth.utils.TokenUtils;
import com.pmolinav.prediction.auth.TokenConfig;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class JwtValidationFilter extends BasicAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtValidationFilter.class);

    private final TokenConfig tokenConfig;

    public JwtValidationFilter(AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        super(authenticationManager);
        this.tokenConfig = tokenConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        String correlationUid = request.getHeader(MDCCommonKeys.CORRELATION_UID.key());
        if (correlationUid == null || correlationUid.isBlank()) {
            correlationUid = UUID.randomUUID().toString();
        }
        MDC.put(MDCCommonKeys.CORRELATION_UID.key(), correlationUid);
        logger.info("Incoming call: {} {}. Query: {}. Correlation-Uid: {}",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), correlationUid);

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
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            MDC.put(MDCCommonKeys.ELAPSED_TIME.key(), String.valueOf(elapsed));

            logger.info("Outgoing call: {} {}. Response Status: {}. Correlation-Uid: {}. Elapsed-time: {}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), correlationUid, elapsed);

            MDC.clear();
        }
    }
}

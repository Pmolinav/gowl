package com.pmolinav.auth.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.auth.auth.TokenConfig;
import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.auth.services.UserTokenAsyncService;
import com.pmolinav.auth.utils.TokenUtils;
import com.pmolinav.userslib.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final UserTokenAsyncService userTokenAsyncService;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   TokenConfig tokenConfig,
                                   UserTokenAsyncService userTokenAsyncService) {
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
        this.userTokenAsyncService = userTokenAsyncService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String username = null;
        String password = null;

        long start = System.currentTimeMillis();
        String correlationUid = request.getHeader(MDCCommonKeys.CORRELATION_UID.key());
        if (correlationUid == null || correlationUid.isBlank()) {
            correlationUid = UUID.randomUUID().toString();
        }
        MDC.put(MDCCommonKeys.CORRELATION_UID.key(), correlationUid);

        logger.info("Attempt Authentication. Incoming call: {} {}. Query: {}. Correlation-Uid: {}",
                request.getMethod(), request.getRequestURI(), request.getQueryString(), correlationUid);
        try {
            UserDTO user = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);
            username = user.getUsername();
            password = user.getPassword();

            MDC.put(MDCCommonKeys.USERNAME.key(), username);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            MDC.put(MDCCommonKeys.ELAPSED_TIME.key(), String.valueOf(elapsed));

            logger.info("Outgoing call: {} {}. Response Status: {}. Correlation-Uid: {}. Elapsed-time: {}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), correlationUid, elapsed);

            MDC.clear();
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal())
                .getUsername();

        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        TokenUtils tokenUtils = new TokenUtils(
                this.tokenConfig.getSecret(),
                this.tokenConfig.getValiditySeconds(),
                this.tokenConfig.getRefreshValiditySeconds()
        );

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isBlank()) {
            userAgent = "unknown";
        }

        MDC.put(MDCCommonKeys.USER_IP.key(), ipAddress);
        MDC.put(MDCCommonKeys.USER_AGENT.key(), userAgent);

        logger.info("Successful authentication for user {} with ip {} and agent {}", username, ipAddress, userAgent);

        String accessToken = tokenUtils.createToken(username, roles);
        String refreshToken = tokenUtils.createRefreshToken(username);

        // Async call to save the new refresh token.
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(tokenConfig.getRefreshValiditySeconds() / 1000);
        userTokenAsyncService.saveUserTokenAsync(username, null, refreshToken, userAgent, ipAddress, expiresAt);

        Map<String, Object> body = new HashMap<>();
        body.put("token", accessToken);
        body.put("refreshToken", refreshToken);
        body.put("message", String.format("Welcome %s, init session was successful!", username));
        body.put("username", username);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Authentication error. Incorrect user or password.");
        body.put("error", failed.getMessage());

        logger.warn("Unsuccessful authentication with message {}", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
    }

}

package com.pmolinav.auth.controllers;


import com.pmolinav.auth.auth.TokenConfig;
import com.pmolinav.auth.auth.interceptors.UserAccessInterceptor;
import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.auth.services.UserTokenAsyncService;
import com.pmolinav.auth.utils.TokenUtils;
import com.pmolinav.userslib.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


//TODO: ORDER SWAGGER WITHOUT USING NUMBERS IN TAGS TO PUT LOGIN AT FIRST.
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("login")
@Tag(name = "2. Login", description = "The Login Controller. Required to authorize users. A valid token is granted and allows valid users to call other controllers with permissions.")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final TokenConfig tokenConfig;

    @Autowired
    private final UserTokenAsyncService userTokenAsyncService;

    // This method replicates JwtAuthenticationFilter logic and is used for unit testing and Swagger.
    @PostMapping
    @Operation(summary = "Authorize user", description = "This is a public endpoint. Authentication is not required to call, but requested user must be registered.")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO, HttpServletRequest request) {
        try {
            logger.info("LoginController: login. Request body: {}", userDTO);
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getRemoteAddr();
            }

            String userAgent = request.getHeader("User-Agent");
            if (userAgent == null || userAgent.isBlank()) {
                userAgent = "unknown";
            }
            // Try to simulate authentication.
            Authentication authResult = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authResult);

            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            TokenUtils tokenUtils = new TokenUtils(
                    tokenConfig.getSecret(),
                    tokenConfig.getValiditySeconds(),
                    tokenConfig.getRefreshValiditySeconds()
            );

            String accessToken = tokenUtils.createToken(username, authResult.getAuthorities());
            String refreshToken = tokenUtils.createRefreshToken(username);

            // Async call to save the new refresh token.
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(tokenConfig.getRefreshValiditySeconds() / 1000);
            userTokenAsyncService.saveUserTokenAsync(username, null, refreshToken, userAgent, ipAddress, expiresAt);

            Map<String, String> response = new HashMap<>();
            response.put("token", accessToken);
            response.put("refreshToken", refreshToken);
            response.put("message", String.format("Welcome %s, init session was successful!", username));
            response.put("username", username);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            if (e instanceof AuthenticationException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }
}

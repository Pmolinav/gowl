package com.pmolinav.auth.controllers;


import com.pmolinav.auth.auth.TokenConfig;
import com.pmolinav.auth.services.UserTokenAsyncService;
import com.pmolinav.auth.utils.TokenUtils;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("refresh")
@Tag(name = "3. Refresh Token", description = "The Refresh Token Controller. The token is refreshed and allows valid users to continue with their sessions and call other controllers with permissions.")
public class RefreshTokenController {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final TokenConfig tokenConfig;

    @Autowired
    private final UserTokenAsyncService userTokenAsyncService;

    @PostMapping
    @Operation(summary = "Authorize user", description = "This is a public endpoint. Authentication is not required to call, but requested user must be registered.")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> tokenRequest,
                                          HttpServletRequest request) {

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isBlank()) {
            userAgent = "unknown";
        }
        String refreshToken = tokenRequest.get("refreshToken");

        try {
            TokenUtils tokenUtils = new TokenUtils(
                    tokenConfig.getSecret(),
                    tokenConfig.getValiditySeconds(),
                    tokenConfig.getRefreshValiditySeconds()
            );

            String newAccessToken = tokenUtils.refreshAccessToken(refreshToken);
            String username = tokenUtils.parseToken(refreshToken).getSubject();
            String newRefreshToken = tokenUtils.createRefreshToken(username);

            // Async call to save the new refresh token.
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(tokenConfig.getRefreshValiditySeconds() / 1000);
            userTokenAsyncService.saveUserTokenAsync(username, newRefreshToken, userAgent, ipAddress, expiresAt);


            Map<String, String> response = new HashMap<>();
            response.put("token", newAccessToken);
            response.put("refreshToken", newRefreshToken);

            return ResponseEntity.ok(response);
        } catch (JwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired refresh token"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error generating token"));
        }
    }
}
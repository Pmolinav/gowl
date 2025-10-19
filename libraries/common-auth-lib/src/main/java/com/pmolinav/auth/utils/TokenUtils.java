package com.pmolinav.auth.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.auth.mixin.SimpleGrantedAuthorityMixin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TokenUtils {

    private final SecretKey secretKey;
    private final Long validitySeconds;
    private final Long refreshValiditySeconds;

    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TokenUtils(String secret, Long validitySeconds, Long refreshValiditySeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.validitySeconds = validitySeconds;
        this.refreshValiditySeconds = refreshValiditySeconds;
        this.objectMapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
    }

    // Create access token.
    public String createToken(String username, Collection<? extends GrantedAuthority> roles) throws JsonProcessingException {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("authorities", objectMapper.writeValueAsString(roles))
                .add("admin", roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN")))
                .add("username", username)
                .add("type", ACCESS_TOKEN_TYPE)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + validitySeconds))
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }

    // Create refresh token.
    public String createRefreshToken(String username) {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("type", REFRESH_TOKEN_TYPE)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshValiditySeconds))
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }

    // Parse and validate token.
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Refresh access token with a valid refresh token.
    public String refreshAccessToken(String refreshToken) throws IOException {
        Claims claims = parseToken(refreshToken);

        if (!REFRESH_TOKEN_TYPE.equals(claims.get("type"))) {
            throw new IllegalArgumentException("Invalid token type for refresh");
        }

        String username = claims.getSubject();

        // TODO: Get Roles from database if needed.
        List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return createToken(username, roles);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) throws IOException {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String authoritiesClaims = claims.get("authorities", String.class);
        String username = claims.getSubject();

        Collection<? extends GrantedAuthority> authorities = Arrays
                .asList(objectMapper.readValue(authoritiesClaims, SimpleGrantedAuthority[].class));

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}

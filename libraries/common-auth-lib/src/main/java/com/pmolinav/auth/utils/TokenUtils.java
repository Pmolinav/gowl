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

public class TokenUtils {

    private final SecretKey secretKey;
    private final Long validitySeconds;

    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TokenUtils(String secret, Long validitySeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.validitySeconds = validitySeconds;
        this.objectMapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
    }

    public String createToken(String username, Collection<? extends GrantedAuthority> roles) throws JsonProcessingException {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("authorities", objectMapper.writeValueAsString(roles))
                .add("admin", roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN")))
                .add("username", username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + validitySeconds))
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
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

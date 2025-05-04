package com.pmolinav.users.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.users.models.request.Role;
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

    //TODO: Get them from application.properties.
    private final static String ACCESS_TOKEN_SECRET = "c7eD5hYnJnVr3uFTh5WTG2XKj6qbBszvuztf8WbCcJY";
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 3600000L;

    public final static SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            ACCESS_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8)
    );
    public final static String PREFIX_TOKEN = "Bearer ";
    public final static String HEADER_AUTHORIZATION = "Authorization";

    public static String createToken(String username, Collection<? extends GrantedAuthority> roles) throws JsonProcessingException {
        Claims claims = Jwts.claims()
                .subject(username)
                .add("authorities", new ObjectMapper().writeValueAsString(roles))
                .add("isAdmin", roles.stream().anyMatch(r -> r.getAuthority().equals(Role.ROLE_ADMIN.name())))
                .add("username", username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS))
                .build();

        return Jwts.builder()
                .claims(claims)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token) throws IOException {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Object authoritiesClaims = claims.get("authorities");
        String username = claims.getSubject();
        Object username2 = claims.get("username");
        System.out.println(username);
        System.out.println(username2);

        Collection<? extends GrantedAuthority> authorities = Arrays
                .asList(new ObjectMapper()
                        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                        .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

}

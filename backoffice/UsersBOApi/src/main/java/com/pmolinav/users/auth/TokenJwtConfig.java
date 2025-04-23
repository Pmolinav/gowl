package com.pmolinav.users.auth;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class TokenJwtConfig {

    public final static SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "tu-clave-secreta-muy-larga-de-al-menos-256-bits".getBytes(StandardCharsets.UTF_8)
    );
    public final static String PREFIX_TOKEN = "Bearer ";
    public final static String HEADER_AUTHORIZATION = "Authorization";

}

package com.pmolinav.auth.controllers;


import com.pmolinav.auth.auth.TokenConfig;
import com.pmolinav.auth.utils.TokenUtils;
import com.pmolinav.userslib.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


//TODO: ORDER SWAGGER WITHOUT USING NUMBERS IN TAGS TO PUT LOGIN AT FIRST.
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("login")
@Tag(name = "2. Login", description = "The Login Controller. Required to authorize users. A valid token is granted and allows valid users to call other controllers with permissions.")
public class LoginController {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final TokenConfig tokenConfig;

    @PostMapping()
    @Operation(summary = "Authorize user", description = "This is a public endpoint. Authentication is not required to call, but requested user must be registered.")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO request) {
        try {
            // Try to simulate authentication.
            Authentication authResult = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION,
                            "Bearer " + new TokenUtils(
                                    this.tokenConfig.getSecret(),
                                    this.tokenConfig.getValiditySeconds()
                            ).createToken(request.getUsername(), authResult.getAuthorities()))
                    .build();
        } catch (Exception e) {
            if (e instanceof AuthenticationException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}

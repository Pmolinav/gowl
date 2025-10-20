package com.pmolinav.auth.controllers;


import com.pmolinav.auth.services.UserTokenAsyncService;
import com.pmolinav.userslib.dto.LogoutDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("auth")
@Tag(name = "4. Logout", description = "The Logout Controller. The token is removed and valid users are logged out.")
public class LogoutController {

    @Autowired
    private UserTokenAsyncService userTokenAsyncService;

    @PostMapping("/logout")
    @Operation(summary = "Logout user from device",
            description = "This is a public endpoint. Authentication is not required to call, but requested user must be registered.")
    public ResponseEntity<?> logout(@RequestBody LogoutDTO logoutDTO) {
        userTokenAsyncService.invalidateToken(logoutDTO);
        return ResponseEntity.ok(Map.of("message", "Session closed successfully"));
    }

    @DeleteMapping("/logout/all")
    @Operation(summary = "Logout user from all devices",
            description = "This is a public endpoint. Authentication is not required to call, but requested user must be registered.")
    public ResponseEntity<?> logoutAll(@RequestParam String username) {
        userTokenAsyncService.invalidateAllTokens(username);
        return ResponseEntity.ok(Map.of("message", "All sessions closed successfully"));
    }
}
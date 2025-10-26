package com.pmolinav.auth.controllers;


import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.exceptions.NotFoundException;
import com.pmolinav.auth.services.UserTokenAsyncService;
import com.pmolinav.userslib.dto.LogoutDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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

    private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

    @Autowired
    private UserTokenAsyncService userTokenAsyncService;

    @PostMapping("/logout")
    @Operation(summary = "Logout user from device",
            description = "This is a public endpoint. Authentication is not required to call, but requested user must be registered.")
    public ResponseEntity<?> logout(@RequestParam String requestUid,
                                    @RequestBody LogoutDTO logoutDTO) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("LogoutController: logout. Request body: {}", logoutDTO);
            userTokenAsyncService.invalidateToken(logoutDTO);
            return ResponseEntity.ok(Map.of("message", "Session closed successfully"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }

    @DeleteMapping("/logout/all")
    @Operation(summary = "Logout user from all devices",
            description = "This is a public endpoint. Authentication is not required to call, but requested user must be registered.")
    public ResponseEntity<?> logoutAll(@RequestParam String requestUid,
                                       @RequestParam String username) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("LogoutController: logoutAll. Params: username: {}", username);
            userTokenAsyncService.invalidateAllTokens(username);
            return ResponseEntity.ok(Map.of("message", "All sessions closed successfully"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }
}
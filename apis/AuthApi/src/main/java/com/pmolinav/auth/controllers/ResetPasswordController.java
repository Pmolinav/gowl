package com.pmolinav.auth.controllers;


import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.exceptions.NotFoundException;
import com.pmolinav.auth.services.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


//TODO: ORDER SWAGGER WITHOUT USING NUMBERS IN TAGS TO PUT LOGIN AT FIRST.
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("auth")
@Tag(name = "5. Reset Password", description = "Reset Password Controller. This endpoints will be used to reset user's password.")
public class ResetPasswordController {

    @Autowired
    private final PasswordResetService passwordResetService;

    @PostMapping("/send-code")
    @Operation(summary = "Request code to email",
            description = "An user request a verification code (OTP) to reset password. It will be sent by email.")
    public ResponseEntity<?> forgotPassword(@RequestParam String email, HttpServletRequest request) {
        try {
            String ip = request.getRemoteAddr();
            passwordResetService.sendResetCode(email, ip);
            return ResponseEntity.ok(Map.of("message", "Verification code successfully sent"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/validate-code")
    @Operation(summary = "Validate verification code for email",
            description = "The users try to validate their verification codes.")
    public ResponseEntity<?> validateCode(@RequestParam String email, @RequestParam String code) {
        try {
            boolean valid = passwordResetService.validateCode(email, code);
            return valid ?
                    ResponseEntity.ok(Map.of("valid", true)) :
                    ResponseEntity.badRequest().body(Map.of("valid", false, "message", "Invalid or expired code"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PutMapping("/update-password")
    @Operation(summary = "Update Password",
            description = "An user tries to update password with previously validated token.")
    public ResponseEntity<?> updatePassword(@RequestParam String token,
                                            @RequestParam String newPassword) {
        try {
            passwordResetService.updatePassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

}

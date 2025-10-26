package com.pmolinav.auth.controllers;


import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.exceptions.NotFoundException;
import com.pmolinav.auth.services.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordController.class);

    @Autowired
    private final PasswordResetService passwordResetService;

    @PostMapping("/send-code")
    @Operation(summary = "Request code to email",
            description = "An user request a verification code (OTP) to reset password. It will be sent by email.")
    public ResponseEntity<?> forgotPasswordSendCode(@RequestParam String requestUid,
                                                    @RequestParam String email,
                                                    HttpServletRequest request) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("ResetPasswordController: forgotPasswordSendCode. Params: email: {}", email);
            String ip = request.getRemoteAddr();
            passwordResetService.sendResetCode(email, ip);
            return ResponseEntity.ok(Map.of("message", "Verification code successfully sent"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }

    @PostMapping("/validate-code")
    @Operation(summary = "Validate verification code for email",
            description = "The users try to validate their verification codes.")
    public ResponseEntity<?> validateCode(@RequestParam String requestUid,
                                          @RequestParam String email,
                                          @RequestParam String code,
                                          HttpServletRequest request) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("ResetPasswordController: validateCode. Params: email: {}, code: {}", email, code);
            String ip = request.getRemoteAddr();
            String token = passwordResetService.validateCode(email, code, ip);
            return token != null ?
                    ResponseEntity.ok(Map.of("valid", true, "token", token)) :
                    ResponseEntity.badRequest().body(Map.of("valid", false, "message", "Invalid or expired code"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }

    @PutMapping("/update-password")
    @Operation(summary = "Update Password",
            description = "An user tries to update password with previously validated token.")
    public ResponseEntity<?> updatePassword(@RequestParam String requestUid,
                                            @RequestParam String email,
                                            @RequestParam String newPassword,
                                            @RequestParam String token) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("ResetPasswordController: updatePassword. Params: email: {}, newPassword: ***, token: {}", email, token);
            passwordResetService.updatePassword(email, newPassword, token);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }

}

package com.pmolinav.auth.controllers;


import com.pmolinav.auth.auth.SpringSecurityConfig;
import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.exceptions.NotFoundException;
import com.pmolinav.auth.services.UserService;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


//TODO: ORDER SWAGGER WITHOUT USING NUMBERS IN TAGS TO PUT LOGIN AT FIRST.
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("users")
@Tag(name = "2. Users", description = "The Users Controller. Required to create an user. A valid token is granted and allows valid users to call other controllers with permissions.")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    @Operation(summary = "Create a new user", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createUser(@RequestParam String requestUid,
                                        @Valid @RequestBody UserDTO userDTO,
                                        BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            // Encode password before save user.
            userDTO.setPassword(SpringSecurityConfig.passwordEncoder().encode(userDTO.getPassword()));
            Long createdUserId = userService.createUser(userDTO);
            return new ResponseEntity<>(createdUserId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a specific user by Id", description = "Bearer token is required to authorize users.")
    public ResponseEntity<User> findUserById(@RequestParam String requestUid, @PathVariable long id) {
        try {
            User user = userService.findUserById(id);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #username.equals(authentication.principal)")
    @GetMapping("/username/{username}")
    @Operation(summary = "Get a specific user by username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<User> findUserByUsername(@RequestParam String requestUid, @PathVariable String username) {
        try {
            User user = userService.findUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete an user by Id", description = "Bearer token is required to authorize users.")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam String requestUid, @PathVariable long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}

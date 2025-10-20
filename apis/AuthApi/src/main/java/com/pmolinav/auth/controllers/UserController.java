package com.pmolinav.auth.controllers;


import com.pmolinav.auth.auth.SpringSecurityConfig;
import com.pmolinav.auth.exceptions.CustomStatusException;
import com.pmolinav.auth.exceptions.NotFoundException;
import com.pmolinav.auth.services.UserService;
import com.pmolinav.userslib.dto.UpdatePasswordDTO;
import com.pmolinav.userslib.dto.UpdateUserDTO;
import com.pmolinav.userslib.dto.UserPublicDTO;
import com.pmolinav.userslib.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


//TODO: ORDER SWAGGER WITHOUT USING NUMBERS IN TAGS TO PUT LOGIN AT FIRST.
@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("users")
@Tag(name = "6. Users", description = "The Users Controller. Required to create an user. A valid token is granted and allows valid users to call other controllers with permissions.")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createUser(@RequestParam String requestUid,
                                        @Valid @RequestBody UserPublicDTO userDTO,
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
            return new ResponseEntity<>(e.getStatusCode());
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
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get a specific user by username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> findUserByUsername(@RequestParam String requestUid, @PathVariable String username) {
        String authUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authUser.equals(username)) {
            return new ResponseEntity<>("Username in request does not match authenticated user", HttpStatus.FORBIDDEN);
        }
        try {
            User user = userService.findUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PutMapping("/username/{username}")
    @Operation(summary = "Update specific user by username",
            description = "Bearer token is required to authorize users. Only the affected user or ADMIN users are Authorized")
    public ResponseEntity<?> updateUserByUsername(@RequestParam String requestUid,
                                                  @PathVariable String username,
                                                  @Valid @RequestBody UpdateUserDTO updateUserDTO,
                                                  BindingResult result) {
        String authUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authUser.equals(username)) {
            return new ResponseEntity<>("Username in request does not match authenticated user", HttpStatus.FORBIDDEN);
        }
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            userService.updateUserByUsername(username, updateUserDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PutMapping("/username/{username}/password")
    @Operation(summary = "Update password for specific user by username",
            description = "Bearer token is required to authorize users. Only the affected user or ADMIN users are Authorized")
    public ResponseEntity<?> updateUserPasswordByUsername(@RequestParam String requestUid,
                                                          @PathVariable String username,
                                                          @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO,
                                                          BindingResult result) {
        String authUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authUser.equals(username)) {
            return new ResponseEntity<>("Username in request does not match authenticated user", HttpStatus.FORBIDDEN);
        }
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            userService.updateUserPasswordByUsername(username, updatePasswordDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
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
            return new ResponseEntity<>(e.getStatusCode());
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

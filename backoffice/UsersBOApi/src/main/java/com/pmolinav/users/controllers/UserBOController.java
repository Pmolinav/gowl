package com.pmolinav.users.controllers;

import com.pmolinav.users.auth.SpringSecurityConfig;
import com.pmolinav.users.exceptions.CustomStatusException;
import com.pmolinav.users.exceptions.NotFoundException;
import com.pmolinav.users.services.UserBOService;
import com.pmolinav.userslib.dto.UpdatePasswordDTO;
import com.pmolinav.userslib.dto.UpdateUserDTO;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("users")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "3. User", description = "The User Controller. Contains all the operations that can be performed on an user.")
public class UserBOController {

    @Autowired
    private UserBOService userBOService;

    //TODO: Pagination
    @GetMapping
    @Operation(summary = "Retrieve all users", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<User>> findAllUsers(@RequestParam String requestUid) {
        try {
            List<User> users = userBOService.findAllUsers();
            return ResponseEntity.ok(users);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

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
            Long createdUserId = userBOService.createUser(userDTO);
            return new ResponseEntity<>(createdUserId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a specific user by Id", description = "Bearer token is required to authorize users.")
    public ResponseEntity<User> getUserById(@RequestParam String requestUid, @PathVariable long id) {
        try {
            User user = userBOService.findUserById(id);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #username.equals(authentication.principal)")
    @GetMapping("/username/{username}")
    @Operation(summary = "Get a specific user by username",
            description = "Bearer token is required to authorize users. Only the affected user or ADMIN users are Authorized")
    public ResponseEntity<User> getUserByUsername(@RequestParam String requestUid, @PathVariable String username) {
        try {
            User user = userBOService.findUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update specific user by user Id",
            description = "Bearer token is required to authorize users. Only the affected user or ADMIN users are Authorized")
    public ResponseEntity<?> updateUserById(@RequestParam String requestUid,
                                            @PathVariable long id,
                                            @Valid @RequestBody UpdateUserDTO updateUserDTO,
                                            BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            userBOService.updateUserById(id, updateUserDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #username.equals(authentication.principal)")
    @PutMapping("/username/{username}")
    @Operation(summary = "Update specific user by username",
            description = "Bearer token is required to authorize users. Only the affected user or ADMIN users are Authorized")
    public ResponseEntity<?> updateUserByUsername(@RequestParam String requestUid,
                                                  @PathVariable String username,
                                                  @Valid @RequestBody UpdateUserDTO updateUserDTO,
                                                  BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            userBOService.updateUserByUsername(username, updateUserDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Update password for specific user by user Id",
            description = "Bearer token is required to authorize users. Only the affected user or ADMIN users are Authorized")
    public ResponseEntity<?> updateUserPasswordById(@RequestParam String requestUid,
                                                    @PathVariable long id,
                                                    @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO,
                                                    BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            userBOService.updateUserPasswordById(id, updatePasswordDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #username.equals(authentication.principal)")
    @PutMapping("/username/{username}/password")
    @Operation(summary = "Update password for specific user by username",
            description = "Bearer token is required to authorize users. Only the affected user or ADMIN users are Authorized")
    public ResponseEntity<?> updateUserPasswordByUsername(@RequestParam String requestUid,
                                                          @PathVariable String username,
                                                          @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO,
                                                          BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            userBOService.updateUserPasswordByUsername(username, updatePasswordDTO);
            return ResponseEntity.ok().build();
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
            userBOService.deleteUser(id);
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

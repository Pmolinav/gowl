package com.pmolinav.users.controllers;

import com.pmolinav.users.auth.SpringSecurityConfig;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.users.services.UserBOService;
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

    // TODO: Maybe a new exposed API will be needed.
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #username.equals(authentication.principal)")
    @GetMapping("/username/{username}")
    @Operation(summary = "Get a specific user by username", description = "Bearer token is required to authorize users.")
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

    //TODO: Complete
//    @PutMapping("{id}")
//    @Operation(summary = "Update a specific user", description = "Bearer token is required to authorize users.")
//    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody UserDTO userDetails) {
//
//        String message = validateMandatoryFieldsInRequest(userDetails);
//
//        try {
//            User updatedUser = userService.findById(id);
//
//            if (!StringUtils.hasText(message)) {
//                updatedUser.setUsername(userDetails.getUsername());
//                // Encode password before update user.
//                updatedUser.setPassword(WebSecurityConfig.passwordEncoder().encode(userDetails.getPassword()));
//                if (userDetails.getRole() != null) {
//                    updatedUser.setRole(userDetails.getRole().name());
//                }
//                userService.createUser(updatedUser);
//                return ResponseEntity.ok(updatedUser);
//            } else {
//                throw new BadRequestException(message);
//            }
//        } catch (NotFoundException e) {
//            logger.error(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//    }

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

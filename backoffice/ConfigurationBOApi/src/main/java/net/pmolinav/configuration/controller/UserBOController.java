package net.pmolinav.configuration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.pmolinav.bookingslib.dto.UserDTO;
import net.pmolinav.bookingslib.exception.NotFoundException;
import net.pmolinav.bookingslib.exception.CustomStatusException;
import net.pmolinav.bookingslib.model.User;
import net.pmolinav.configuration.security.WebSecurityConfig;
import net.pmolinav.configuration.service.UserBOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("users")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "3. User", description = "The User Controller. Contains all the operations that can be performed on an user.")
public class UserBOController {

    @Autowired
    private UserBOService userBOService;

    //TODO: Validar JSON con @Valid y BindingResult. Añadir validaciones en los DTOs.

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
    public ResponseEntity<Long> createUser(@RequestParam String requestUid, @Valid @RequestBody UserDTO userDTO) {
        try {
            // Encode password before save user.
            userDTO.setPassword(WebSecurityConfig.passwordEncoder().encode(userDTO.getPassword()));
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

}

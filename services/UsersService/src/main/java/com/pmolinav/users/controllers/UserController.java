package com.pmolinav.users.controllers;

import com.pmolinav.users.exceptions.CustomStatusException;
import com.pmolinav.users.exceptions.InternalServerErrorException;
import com.pmolinav.users.exceptions.NotFoundException;
import com.pmolinav.users.services.UserService;
import com.pmolinav.userslib.dto.UpdatePasswordDTO;
import com.pmolinav.userslib.dto.UpdateUserDTO;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        try {
            List<User> users = userService.findAllUsers();
            return ResponseEntity.ok(users);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody UserDTO userDTO) {
        try {
            User createdUser = userService.createUser(userDTO);

//            userService.storeInKafka(ChangeType.CREATE, createdUser.getUserId(), createdUser);

            return new ResponseEntity<>(createdUser.getUserId(), HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<User> findUserById(@PathVariable long id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("username/{username}")
    public ResponseEntity<User> findUserByUsername(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        try {
            boolean exists = userService.existsByEmail(email);
            return ResponseEntity.ok(exists);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable long id,
                                            @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            userService.updateUserById(id, updateUserDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/username/{username}")
    public ResponseEntity<?> updateUserByUsername(@PathVariable String username,
                                                  @RequestBody UpdateUserDTO updateUserDTO) {
        try {
            userService.updateUserByUsername(username, updateUserDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updateUserPasswordById(@PathVariable long id,
                                                    @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        try {
            User user = userService.updateUserPasswordById(id, updatePasswordDTO);
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/username/{username}/password")
    public ResponseEntity<?> updateUserPasswordByUsername(@PathVariable String username,
                                                          @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        try {
            User user = userService.updateUserPasswordByUsername(username, updatePasswordDTO);
            if (user == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/email/{email}/password")
    public ResponseEntity<?> updateUserPasswordByEmail(@PathVariable String email,
                                                       @RequestParam String newPassword) {
        try {
            userService.updateUserPasswordByEmail(email, newPassword);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        try {
            userService.deleteUser(id);

//            userService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

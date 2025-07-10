package com.pmolinav.users.clients;

import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "usersservice")
public interface UserClient {

    @GetMapping("/users/health")
    void health();

    @GetMapping("/users")
    List<User> findAllUsers();

    @PostMapping("/users")
    Long createUser(@RequestBody UserDTO userDTO);

    @GetMapping("/users/{id}")
    User findUserById(@PathVariable long id);

    @GetMapping("/users/username/{username}")
    User findUserByUsername(@PathVariable String username);

//    @PutMapping("/users/{id}")
//    User updateUser(@PathVariable long id, @RequestBody UserUpdateDTO userDetails);

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable long id);
}

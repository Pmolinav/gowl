package com.pmolinav.users.clients;

import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "UserClient")
public interface UserClient {

    @GetMapping("/health")
    void health();

    @GetMapping
    List<User> findAllUsers();

    @PostMapping
    Long createUser(@RequestBody UserDTO userDTO);

    @GetMapping("/{id}")
    User findUserById(@PathVariable long id);

    @GetMapping("/username/{username}")
    User findUserByUsername(@PathVariable String username);

//    @PutMapping("/{id}")
//    User updateUser(@PathVariable long id, @RequestBody UserUpdateDTO userDetails);

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable long id);
}

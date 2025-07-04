package com.pmolinav.auth.clients;

import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "UserService", url = "usersservice:8001/users")
public interface UserClient {

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

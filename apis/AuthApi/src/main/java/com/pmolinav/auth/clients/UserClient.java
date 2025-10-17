package com.pmolinav.auth.clients;

import com.pmolinav.userslib.dto.UpdatePasswordDTO;
import com.pmolinav.userslib.dto.UpdateUserDTO;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "UserService", url = "usersservice:8001/users")
public interface UserClient {

    @PostMapping
    Long createUser(@RequestBody UserDTO userDTO);

    @GetMapping("/{id}")
    User findUserById(@PathVariable long id);

    @GetMapping("/username/{username}")
    User findUserByUsername(@PathVariable String username);

    @PutMapping("/username/{username}")
    void updateUserByUsername(@PathVariable String username, @RequestBody UpdateUserDTO updateUserDTO);

    @PutMapping("/username/{username}/password")
    void updateUserPasswordByUsername(@PathVariable String username, @RequestBody UpdatePasswordDTO updatePasswordDTO);

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable long id);
}

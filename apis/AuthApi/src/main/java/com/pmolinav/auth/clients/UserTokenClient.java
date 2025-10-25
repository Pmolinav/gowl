package com.pmolinav.auth.clients;

import com.pmolinav.userslib.dto.LogoutDTO;
import com.pmolinav.userslib.dto.UserTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "UserTokenClient", url = "usersservice:8001/tokens")
public interface UserTokenClient {

    @GetMapping("/exists/username/{username}")
    boolean existsTokenForUser(@PathVariable String username, @RequestParam String token);

    @PostMapping
    void saveUserToken(@RequestBody UserTokenDTO userToken);

    @PostMapping("/invalidate")
    void invalidateToken(@RequestBody LogoutDTO logoutDTO);

    @DeleteMapping("/invalidate/all")
    void invalidateAllTokens(@RequestParam String username);
}

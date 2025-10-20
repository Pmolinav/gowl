package com.pmolinav.auth.clients;

import com.pmolinav.userslib.dto.LogoutDTO;
import com.pmolinav.userslib.dto.UserTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "UserTokenClient", url = "usersservice:8001/users")
public interface UserTokenClient {

    @PostMapping("/tokens")
    void saveUserToken(@RequestBody UserTokenDTO userToken);

    @PostMapping("/tokens/invalidate")
    void invalidateToken(@RequestBody LogoutDTO logoutDTO);

    @DeleteMapping("/tokens/invalidate/all")
    void invalidateAllTokens(@RequestParam String username);
}

package com.pmolinav.auth.clients;

import com.pmolinav.userslib.dto.UserTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "UserTokenClient", url = "usersservice:8001/users")
public interface UserTokenClient {

    @PostMapping("/tokens")
    void saveUserToken(@RequestBody UserTokenDTO userToken);
}

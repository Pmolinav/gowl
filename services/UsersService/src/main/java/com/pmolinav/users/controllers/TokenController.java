package com.pmolinav.users.controllers;

import com.pmolinav.users.exceptions.CustomStatusException;
import com.pmolinav.users.exceptions.InternalServerErrorException;
import com.pmolinav.users.exceptions.NotFoundException;
import com.pmolinav.users.services.TokenService;
import com.pmolinav.userslib.dto.*;
import com.pmolinav.userslib.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("tokens")
public class TokenController {

    @Autowired
    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<Void> saveToken(@RequestBody UserTokenDTO userToken) {
        try {
            tokenService.saveToken(userToken);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("invalidate")
    public ResponseEntity<Void> invalidateToken(@RequestBody LogoutDTO logoutDTO) {
        try {
            tokenService.invalidateToken(logoutDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/invalidate/all")
    public ResponseEntity<Void> deleteUser(@RequestParam String username) {
        try {
            tokenService.invalidateAllTokens(username);

//            userService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

package com.pmolinav.leagues.controllers;

import com.pmolinav.leagues.auth.SpringSecurityConfig;
import com.pmolinav.leagues.exceptions.CustomStatusException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.services.LeaguesBOService;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("leagues")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "2. Leagues", description = "The User Controller. Contains all the operations that can be performed on an user.")
public class LeaguesBOController {

    @Autowired
    private LeaguesBOService leaguesBOService;

    //TODO: Pagination
    @GetMapping
    @Operation(summary = "Retrieve all leagues", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeagueDTO>> findAllLeagues(@RequestParam String requestUid) {
        try {
            List<LeagueDTO> users = leaguesBOService.findAllLeagues();
            return ResponseEntity.ok(users);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new league", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createLeague(@RequestParam String requestUid,
                                          @Valid @RequestBody LeagueDTO leagueDTO,
                                          BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            // Encode password before save user.
            if (StringUtils.isNotBlank(leagueDTO.getPassword()))
                leagueDTO.setPassword(SpringSecurityConfig.passwordEncoder().encode(leagueDTO.getPassword()));

            Long createdLeagueId = leaguesBOService.createLeague(leagueDTO);
            return new ResponseEntity<>(createdLeagueId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a specific league by Id", description = "Bearer token is required to authorize users.")
    public ResponseEntity<LeagueDTO> getLeagueById(@RequestParam String requestUid, @PathVariable long id) {
        try {
            LeagueDTO league = leaguesBOService.findLeagueById(id);
            return ResponseEntity.ok(league);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/names/{name}")
    @Operation(summary = "Get a specific league by name", description = "Bearer token is required to authorize users.")
    public ResponseEntity<LeagueDTO> getLeagueByName(@RequestParam String requestUid, @PathVariable String name) {
        try {
            LeagueDTO league = leaguesBOService.findLeagueByName(name);
            return ResponseEntity.ok(league);
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
    @Operation(summary = "Delete a league by Id", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteLeague(@RequestParam String requestUid, @PathVariable long id) {
        try {
            leaguesBOService.deleteLeague(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/names/{name}")
    @Operation(summary = "Delete a league by name", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteLeagueByName(@RequestParam String requestUid, @PathVariable String name) {
        try {
            leaguesBOService.deleteLeagueByName(name);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}

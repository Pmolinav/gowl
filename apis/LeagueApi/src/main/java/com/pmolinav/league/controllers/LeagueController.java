package com.pmolinav.league.controllers;

import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.league.auth.SpringSecurityConfig;
import com.pmolinav.league.exceptions.CustomStatusException;
import com.pmolinav.league.exceptions.NotFoundException;
import com.pmolinav.league.services.LeaguesService;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "4. Leagues", description = "The League Controller. Contains all the operations that can be performed on a league.")
public class LeagueController {

    private static final Logger logger = LoggerFactory.getLogger(LeagueController.class);

    @Autowired
    private LeaguesService leaguesService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #leagueDTO.ownerUsername == authentication.principal")
    @PostMapping
    @Operation(summary = "Create a new league", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createLeague(@RequestParam String requestUid,
                                          @Valid @RequestBody LeagueDTO leagueDTO,
                                          BindingResult result) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            MDC.put(MDCCommonKeys.USERNAME.key(), leagueDTO.getOwnerUsername());
            logger.info("LeagueController: createLeague. Request body: {}", leagueDTO);
            if (result.hasErrors()) {
                return validation(result);
            }
            // Encode password before save user.
            if (StringUtils.isNotBlank(leagueDTO.getPassword()))
                leagueDTO.setPassword(SpringSecurityConfig.passwordEncoder().encode(leagueDTO.getPassword()));

            Long createdLeagueId = leaguesService.createLeague(leagueDTO);
            return new ResponseEntity<>(createdLeagueId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
            MDC.remove(MDCCommonKeys.USERNAME.key());
        }
    }

    @PutMapping("close/{id}")
    @Operation(summary = "Close a league by league ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> closeLeagueById(@RequestParam String requestUid,
                                             @PathVariable long id) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            MDC.put(MDCCommonKeys.LEAGUE_ID.key(), String.valueOf(id));
            logger.info("LeagueController: createLeague. Path: id: {}", id);
            leaguesService.closeLeagueById(id);
            return ResponseEntity.ok().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
            MDC.remove(MDCCommonKeys.LEAGUE_ID.key());
        }
    }

    @PutMapping("close/names/{name}")
    @Operation(summary = "Close a league by league name", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> closeLeagueByName(@RequestParam String requestUid,
                                               @PathVariable String name) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("LeagueController: closeLeagueByName. Path: name: {}", name);
            leaguesService.closeLeagueByName(name);
            return ResponseEntity.ok().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a specific league by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<LeagueDTO> getLeagueById(@RequestParam String requestUid,
                                                   @PathVariable long id) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            MDC.put(MDCCommonKeys.LEAGUE_ID.key(), String.valueOf(id));
            logger.info("LeagueController: getLeagueById. Path: id: {}", id);
            LeagueDTO league = leaguesService.findLeagueById(id);
            return ResponseEntity.ok(league);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
            MDC.remove(MDCCommonKeys.LEAGUE_ID.key());
        }
    }

    // TODO: Fix methods which ask 2 times.

    @GetMapping("/names/{name}")
    @Operation(summary = "Get a specific league by name", description = "Bearer token is required to authorize users.")
    public ResponseEntity<LeagueDTO> getLeagueByName(@RequestParam String requestUid,
                                                     @PathVariable String name) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("LeagueController: getLeagueByName. Path: name: {}", name);
            LeagueDTO league = leaguesService.findLeagueByName(name);
            return ResponseEntity.ok(league);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get leagues by username", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeagueDTO>> getLeagueByUsername(@RequestParam String requestUid,
                                                               @PathVariable String username) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            MDC.put(MDCCommonKeys.USERNAME.key(), username);
            logger.info("LeagueController: getLeagueByUsername. Path: username: {}", username);
            List<LeagueDTO> leagues = leaguesService.findLeaguesByUsername(username);
            return ResponseEntity.ok(leagues);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
            MDC.remove(MDCCommonKeys.USERNAME.key());
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

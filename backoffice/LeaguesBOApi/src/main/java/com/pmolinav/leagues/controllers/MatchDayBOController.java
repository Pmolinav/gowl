package com.pmolinav.leagues.controllers;

import com.pmolinav.leagues.exceptions.CustomStatusException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.services.MatchDaysBOService;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.model.MatchDayId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("match-days")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "3. Match days", description = "The match days Controller. Contains all the operations that can be performed on match days.")
public class MatchDayBOController {

    @Autowired
    private final MatchDaysBOService matchDaysBOService;

    @Autowired
    public MatchDayBOController(MatchDaysBOService matchDaysBOService) {
        this.matchDaysBOService = matchDaysBOService;
    }

    //TODO: Pagination
    @GetMapping
    @Operation(summary = "Retrieve all match days", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<MatchDayDTO>> findAllMatchDays(@RequestParam String requestUid) {
        try {
            List<MatchDayDTO> matchDays = matchDaysBOService.findAllMatchDays();

            return ResponseEntity.ok(matchDays);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/categories/{categoryId}")
    @Operation(summary = "Retrieve match days by category ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<MatchDayDTO>> findMatchDaysByCategoryId(@RequestParam String requestUid,
                                                                       @PathVariable String categoryId) {
        try {
            List<MatchDayDTO> matchDays = matchDaysBOService.findMatchDayByCategoryId(categoryId);

            return ResponseEntity.ok(matchDays);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/categories/{categoryId}/seasons/{season}")
    @Operation(summary = "Retrieve match days by category ID and season", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<MatchDayDTO>> findMatchDaysByCategoryIdAndSeason(@RequestParam String requestUid,
                                                                                @PathVariable String categoryId,
                                                                                @PathVariable Integer season) {
        try {
            List<MatchDayDTO> matchDays = matchDaysBOService.findMatchDayByCategoryIdAndSeason(categoryId, season);
            return ResponseEntity.ok(matchDays);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping
    @Operation(summary = "Create new match day", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createMatchDay(@RequestParam String requestUid,
                                            @RequestBody MatchDayDTO matchDayDTO,
                                            BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            MatchDayId matchDayId = matchDaysBOService.createMatchDay(matchDayDTO);

            return new ResponseEntity<>(matchDayId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping("/bulk")
    @Operation(summary = "Create several match days", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createMatchDays(@RequestParam String requestUid,
                                             @Valid @RequestBody List<MatchDayDTO> matchDayDTOList,
                                             BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            List<MatchDayId> matchDayIds = matchDaysBOService.createMatchDays(matchDayDTOList);

            return new ResponseEntity<>(matchDayIds, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }

    }

    @DeleteMapping("/categories/{categoryId}")
    @Operation(summary = "Delete match days by category ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteMatchDaysByCategoryId(@RequestParam String requestUid,
                                                         @PathVariable String categoryId) {
        try {
            matchDaysBOService.deleteMatchDaysByCategoryId(categoryId);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/categories/{categoryId}/seasons/{season}")
    @Operation(summary = "Delete match days by category ID and season", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteMatchDaysByCategoryIdAndSeason(@RequestParam String requestUid,
                                                                  @PathVariable String categoryId,
                                                                  @PathVariable Integer season) {
        try {
            matchDaysBOService.deleteMatchDaysByCategoryIdAndSeason(categoryId, season);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    @Operation(summary = "Delete match day by category ID, season and number", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteMatchDayByCategoryIdSeasonAndNumber(@RequestParam String requestUid,
                                                                       @PathVariable String categoryId,
                                                                       @PathVariable Integer season,
                                                                       @PathVariable Integer number) {
        try {
            matchDaysBOService.deleteMatchDayByCategoryIdSeasonAndNumber(categoryId, season, number);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
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

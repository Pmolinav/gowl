package com.pmolinav.predictions.controllers;

import com.pmolinav.predictions.exceptions.CustomStatusException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.services.MatchBOService;
import com.pmolinav.predictionslib.dto.MatchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("matches")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "5. Matches", description = "The Match Controller. Contains all the operations that can be performed on a match.")
public class MatchBOController {

    @Autowired
    private MatchBOService matchBOService;

    @GetMapping
    @Operation(summary = "Retrieve all matches", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<MatchDTO>> findAllMatches(@RequestParam String requestUid) {
        try {
            return ResponseEntity.ok(matchBOService.findAllMatches());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific match by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<MatchDTO> findMatchById(@RequestParam String requestUid,
                                                  @PathVariable long id) {
        try {
            return ResponseEntity.ok(matchBOService.findMatchById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    public ResponseEntity<List<MatchDTO>> findByCategoryIdSeasonAndMatchDayNumber(
            @PathVariable String categoryId,
            @PathVariable Integer season,
            @PathVariable Integer number) {
        try {
            List<MatchDTO> matches = matchBOService.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, number);
            return ResponseEntity.ok(matches);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new match", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createMatch(@RequestParam String requestUid,
                                         @Valid @RequestBody MatchDTO matchDTO,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            Long createdId = matchBOService.createMatch(matchDTO);
            return new ResponseEntity<>(createdId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a match", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> updateMatch(@RequestParam String requestUid,
                                         @PathVariable long id,
                                         @Valid @RequestBody MatchDTO matchDTO,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            matchBOService.updateMatch(id, matchDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a match by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteMatch(@RequestParam String requestUid,
                                         @PathVariable long id) {
        try {
            matchBOService.deleteMatch(id);
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

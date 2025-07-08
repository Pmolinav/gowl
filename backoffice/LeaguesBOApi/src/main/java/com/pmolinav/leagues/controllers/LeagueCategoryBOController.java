package com.pmolinav.leagues.controllers;

import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagues.services.LeagueCategoriesBOService;
import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.model.LeagueCategory;
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
@RequestMapping("categories")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "2. League categories", description = "The league categories Controller. Contains all the operations that can be performed on league categories.")
public class LeagueCategoryBOController {

    @Autowired
    private final LeagueCategoriesBOService leagueCategoriesBOService;

    @Autowired
    public LeagueCategoryBOController(LeagueCategoriesBOService leagueCategoriesBOService) {
        this.leagueCategoriesBOService = leagueCategoriesBOService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all league categories", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeagueCategory>> findAllLeagueCategories(@RequestParam String requestUid) {
        try {
            List<LeagueCategory> categories = leagueCategoriesBOService.findAllLeagueCategories();
            return ResponseEntity.ok(categories);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "Get league category by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<LeagueCategory> findLeagueCategoryById(@RequestParam String requestUid,
                                                                 @PathVariable String id) {
        try {
            LeagueCategory leagueCategory = leagueCategoriesBOService.findLeagueCategoryById(id);
            return ResponseEntity.ok(leagueCategory);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new league category", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createLeagueCategory(@RequestParam String requestUid,
                                                  @Valid @RequestBody LeagueCategoryDTO leagueCategoryDTO,
                                                  BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validation(result);
            }
            String categoryId = leagueCategoriesBOService.createLeagueCategory(leagueCategoryDTO);

            return new ResponseEntity<>(categoryId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }

    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete league category", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteLeagueCategory(@RequestParam String requestUid,
                                                  @PathVariable String id) {
        try {
            leagueCategoriesBOService.deleteLeagueCategory(id);

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

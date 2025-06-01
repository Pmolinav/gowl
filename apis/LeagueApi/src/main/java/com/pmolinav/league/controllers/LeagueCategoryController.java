package com.pmolinav.league.controllers;

import com.pmolinav.league.exceptions.CustomStatusException;
import com.pmolinav.league.exceptions.NotFoundException;
import com.pmolinav.league.services.LeagueCategoriesService;
import com.pmolinav.leagueslib.model.LeagueCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("categories")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "2. League categories", description = "The league categories Controller. Contains all the operations that can be performed on league categories.")
public class LeagueCategoryController {

    @Autowired
    private final LeagueCategoriesService leagueCategoriesService;

    @Autowired
    public LeagueCategoryController(LeagueCategoriesService leagueCategoriesService) {
        this.leagueCategoriesService = leagueCategoriesService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all league categories", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<LeagueCategory>> findAllLeagueCategories(@RequestParam String requestUid) {
        try {
            List<LeagueCategory> categories = leagueCategoriesService.findAllLeagueCategories();
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
            LeagueCategory leagueCategory = leagueCategoriesService.findLeagueCategoryById(id);
            return ResponseEntity.ok(leagueCategory);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}

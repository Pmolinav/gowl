package com.pmolinav.leagues.controllers;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.services.LeagueCategoryService;
import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.model.LeagueCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("categories")
public class LeagueCategoryController {

    @Autowired
    private final LeagueCategoryService leagueCategoryService;

    @Autowired
    public LeagueCategoryController(LeagueCategoryService leagueCategoryService) {
        this.leagueCategoryService = leagueCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<LeagueCategory>> findAllLeagueCategories() {
        try {
            List<LeagueCategory> categories = leagueCategoryService.findAllLeagueCategories();
            return ResponseEntity.ok(categories);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createLeagueCategory(@RequestBody LeagueCategoryDTO leagueCategoryDTO) {
        try {
            LeagueCategory createdCategory = leagueCategoryService.createLeagueCategory(leagueCategoryDTO);

//            leaguesService.storeInKafka(ChangeType.CREATE, createdLeagueCategory.getCategoryId(), createdLeagueCategory);

            return new ResponseEntity<>(createdCategory.getCategoryId(), HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<LeagueCategory> findLeagueCategoryById(@PathVariable String id) {
        try {
            LeagueCategory leagueCategory = leagueCategoryService.findById(id);
            return ResponseEntity.ok(leagueCategory);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

// TODO: Complete
//    @PutMapping("{id}")
//    @Operation(summary = "Update a specific league", description = "Bearer token is required to authorize leagues.")
//    public ResponseEntity<LeagueCategory> updateLeagueCategory(@RequestParam String requestUid, @PathVariable long id, @RequestBody LeagueCategoryDTO leaguesDetails) {
//        String message = validateMandatoryFieldsInRequest(leaguesDetails);
//        try {
//            LeagueCategory updatedLeagueCategory = leagueService.findById(id);
//
//            if (!StringUtils.hasText(message)) {
//                updatedLeagueCategory.setName(leagueDetails.getName());
//                updatedLeagueCategory.setDescription(leagueDetails.getDescription());
//                updatedLeagueCategory.setPrice(leagueDetails.getPrice());
//                if (leagueDetails.getType() != null) {
//                    updatedLeagueCategory.setType(leagueDetails.getType().name());
//                }
//                leagueService.createLeagueCategory(updatedLeagueCategory);
//                return ResponseEntity.ok(updatedLeagueCategory);
//            } else {
//                return ResponseEntity.badRequest().build();
//            }
//        } catch (NotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (UnexpectedException e) {
//            return ResponseEntity.status(e.getStatusCode()).build();
//        }
//    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteLeagueCategory(@PathVariable String id) {
        try {
            leagueCategoryService.deleteLeagueCategory(id);

//            leagueService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

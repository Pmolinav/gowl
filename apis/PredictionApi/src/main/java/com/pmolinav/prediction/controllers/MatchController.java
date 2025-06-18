package com.pmolinav.prediction.controllers;

import com.pmolinav.prediction.exceptions.CustomStatusException;
import com.pmolinav.prediction.exceptions.NotFoundException;
import com.pmolinav.prediction.services.MatchService;
import com.pmolinav.predictionslib.dto.MatchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("matches")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "5. Matches", description = "The Match Controller. Contains all the operations that can be performed on a match.")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific match by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<MatchDTO> findMatchById(@RequestParam String requestUid,
                                                  @PathVariable long id) {
        try {
            return ResponseEntity.ok(matchService.findMatchById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    public ResponseEntity<List<MatchDTO>> findByCategoryIdSeasonAndMatchDayNumber(
            @RequestParam String requestUid,
            @PathVariable String categoryId,
            @PathVariable Integer season,
            @PathVariable Integer number) {
        try {
            List<MatchDTO> matches = matchService.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, number);
            return ResponseEntity.ok(matches);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}

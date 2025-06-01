package com.pmolinav.league.controllers;

import com.pmolinav.league.exceptions.CustomStatusException;
import com.pmolinav.league.exceptions.NotFoundException;
import com.pmolinav.league.services.MatchDaysService;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("match-days")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "3. Match days", description = "The match days Controller. Contains all the operations that can be performed on match days.")
public class MatchDayController {

    @Autowired
    private final MatchDaysService matchDaysService;

    @Autowired
    public MatchDayController(MatchDaysService matchDaysService) {
        this.matchDaysService = matchDaysService;
    }

    @GetMapping("/categories/{categoryId}/seasons/{season}")
    @Operation(summary = "Retrieve match days by category ID and season", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<MatchDayDTO>> findMatchDaysByCategoryIdAndSeason(@RequestParam String requestUid,
                                                                                @PathVariable String categoryId,
                                                                                @PathVariable Integer season) {
        try {
            List<MatchDayDTO> matchDays = matchDaysService.findMatchDayByCategoryIdAndSeason(categoryId, season);
            return ResponseEntity.ok(matchDays);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}

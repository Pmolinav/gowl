package com.pmolinav.league.controllers;

import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.league.exceptions.CustomStatusException;
import com.pmolinav.league.exceptions.NotFoundException;
import com.pmolinav.league.services.MatchDaysService;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.dto.SimpleMatchDayDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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

    private static final Logger logger = LoggerFactory.getLogger(MatchDayController.class);

    @Autowired
    private final MatchDaysService matchDaysService;

    @Autowired
    public MatchDayController(MatchDaysService matchDaysService) {
        this.matchDaysService = matchDaysService;
    }

    @GetMapping("/categories/{categoryId}/seasons/{season}")
    @Operation(summary = "Retrieve match days by category ID and season", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<SimpleMatchDayDTO>> findMatchDaysByCategoryIdAndSeason(@RequestParam String requestUid,
                                                                                      @PathVariable String categoryId,
                                                                                      @PathVariable Integer season) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("MatchDayController: findMatchDaysByCategoryIdAndSeason. " +
                    "Path: categoryId: {}, season: {}", categoryId, season);
            List<MatchDayDTO> matchDays = matchDaysService.findMatchDayByCategoryIdAndSeason(categoryId, season);
            return ResponseEntity.ok(matchDays.stream().map(matchDayDTO ->
                    new SimpleMatchDayDTO("J " + matchDayDTO.getMatchDayNumber(),
                            String.valueOf(matchDayDTO.getMatchDayNumber()))).toList()
            );
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }
}

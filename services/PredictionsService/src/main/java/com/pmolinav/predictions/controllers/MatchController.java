package com.pmolinav.predictions.controllers;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.services.MatchService;
import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.model.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("matches")
public class MatchController {

    @Autowired
    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    // TODO: Pagination
    @GetMapping
    public ResponseEntity<List<MatchDTO>> findAllMatches() {
        try {
            List<MatchDTO> matches = matchService.findAllMatches();
            return ResponseEntity.ok(matches);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Long> createMatch(@RequestBody MatchDTO matchDTO) {
        try {
            Match createdMatch = matchService.createMatch(matchDTO);
            return new ResponseEntity<>(createdMatch.getMatchId(), HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchDTO> findByMatchId(@PathVariable Long matchId) {
        try {
            MatchDTO match = matchService.findByMatchId(matchId);
            return ResponseEntity.ok(match);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories/{categoryId}/seasons/{season}/match-day/{matchDayNumber}")
    public ResponseEntity<List<MatchDTO>> findByCategoryIdSeasonAndMatchDayNumber(
            @PathVariable String categoryId,
            @PathVariable Integer season,
            @PathVariable Integer matchDayNumber) {
        try {
            List<MatchDTO> matches = matchService.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, matchDayNumber);
            return ResponseEntity.ok(matches);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> deleteByMatchId(@PathVariable Long matchId) {
        try {
            matchService.deleteByMatchId(matchId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/categories/{categoryId}/seasons/{season}/match-day/{matchDayNumber}")
    public ResponseEntity<Void> deleteByCategoryIdSeasonAndMatchDayNumber(
            @PathVariable String categoryId,
            @PathVariable Integer season,
            @PathVariable Integer matchDayNumber) {
        try {
            matchService.deleteByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, matchDayNumber);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}

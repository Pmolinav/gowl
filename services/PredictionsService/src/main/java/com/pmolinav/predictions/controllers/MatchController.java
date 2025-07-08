package com.pmolinav.predictions.controllers;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMatch(@PathVariable Long id, @RequestBody MatchDTO matchDTO) {
        try {
            matchService.updateMatch(id, matchDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> findByMatchId(@PathVariable Long id) {
        try {
            MatchDTO match = matchService.findByMatchId(id);
            return ResponseEntity.ok(match);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    public ResponseEntity<List<MatchDTO>> findByCategoryIdSeasonAndMatchDayNumber(
            @PathVariable String categoryId,
            @PathVariable Integer season,
            @PathVariable Integer number) {
        try {
            List<MatchDTO> matches = matchService.findByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, number);
            return ResponseEntity.ok(matches);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByMatchId(@PathVariable Long id) {
        try {
            matchService.deleteByMatchId(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    public ResponseEntity<Void> deleteByCategoryIdSeasonAndMatchDayNumber(
            @PathVariable String categoryId,
            @PathVariable Integer season,
            @PathVariable Integer number) {
        try {
            matchService.deleteByCategoryIdAndSeasonAndMatchDayNumber(categoryId, season, number);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}

package com.pmolinav.leagues.controllers;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.services.MatchDayService;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.model.MatchDay;
import com.pmolinav.leagueslib.model.MatchDayId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("match-days")
public class MatchDayController {

    @Autowired
    private final MatchDayService matchDayService;

    @Autowired
    public MatchDayController(MatchDayService matchDayService) {
        this.matchDayService = matchDayService;
    }

    //TODO: Pagination
    @GetMapping
    public ResponseEntity<List<MatchDayDTO>> findAllMatchDays(
            @RequestParam(required = false) Long dateFrom,
            @RequestParam(required = false) Long dateTo,
            @RequestParam(required = false) Boolean synced) {
        try {
            List<MatchDayDTO> matchDays = matchDayService.findAllMatchDays(dateFrom, dateTo, synced);
            return ResponseEntity.ok(matchDays);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<MatchDayId> createMatchDay(@RequestBody MatchDayDTO matchDayDTO) {
        try {
            MatchDay createdMatchDay = matchDayService.createMatchDay(matchDayDTO);

//            leaguesService.storeInKafka(ChangeType.CREATE, createdMatchDay.getCategoryId(), createdMatchDay);

            return new ResponseEntity<>(new MatchDayId(createdMatchDay.getCategoryId(),
                    createdMatchDay.getSeason(), createdMatchDay.getMatchDayNumber()),
                    HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping("/bulk")
    public ResponseEntity<List<MatchDayId>> createMatchDays(@RequestBody List<MatchDayDTO> matchDayDTOList) {
        try {
            List<MatchDay> createdMatchDays = matchDayService.createMatchDays(matchDayDTOList);

//            leaguesService.storeInKafka(ChangeType.CREATE, createdMatchDay.getCategoryId(), createdMatchDay);

            return new ResponseEntity<>(createdMatchDays.stream()
                    .map(matchDay -> new MatchDayId(matchDay.getCategoryId(),
                            matchDay.getSeason(),
                            matchDay.getMatchDayNumber()))
                    .toList(), HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @PutMapping
    public ResponseEntity<Void> updateMatchDay(@RequestBody MatchDayDTO matchDayDTO) {
        try {
            matchDayService.updateMatchDay(matchDayDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<MatchDayDTO>> findMatchDayByCategoryId(@PathVariable String categoryId) {
        try {
            List<MatchDayDTO> matchDays = matchDayService.findByCategoryId(categoryId);
            return ResponseEntity.ok(matchDays);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories/{categoryId}/seasons/{season}")
    public ResponseEntity<List<MatchDayDTO>> findMatchDayByCategoryIdAndSeason(@PathVariable String categoryId,
                                                                               @PathVariable Integer season) {
        try {
            List<MatchDayDTO> matchDays = matchDayService.findByCategoryIdAndSeason(categoryId, season);
            return ResponseEntity.ok(matchDays);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

// TODO: Complete
//    @PutMapping("{id}")
//    @Operation(summary = "Update a specific league", description = "Bearer token is required to authorize leagues.")
//    public ResponseEntity<MatchDay> updateMatchDay(@RequestParam String requestUid, @PathVariable long id, @RequestBody MatchDayDTO leaguesDetails) {
//        String message = validateMandatoryFieldsInRequest(leaguesDetails);
//        try {
//            MatchDay updatedMatchDay = leagueService.findById(id);
//
//            if (!StringUtils.hasText(message)) {
//                updatedMatchDay.setName(leagueDetails.getName());
//                updatedMatchDay.setDescription(leagueDetails.getDescription());
//                updatedMatchDay.setPrice(leagueDetails.getPrice());
//                if (leagueDetails.getType() != null) {
//                    updatedMatchDay.setType(leagueDetails.getType().name());
//                }
//                leagueService.createMatchDay(updatedMatchDay);
//                return ResponseEntity.ok(updatedMatchDay);
//            } else {
//                return ResponseEntity.badRequest().build();
//            }
//        } catch (NotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (UnexpectedException e) {
//            return ResponseEntity.status(e.getStatusCode()).build();
//        }
//    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteMatchDaysByCategoryId(@PathVariable String categoryId) {
        try {
            matchDayService.deleteMatchDaysByCategoryId(categoryId);

//            leagueService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/categories/{categoryId}/seasons/{season}")
    public ResponseEntity<?> deleteMatchDaysByCategoryIdAndSeason(@PathVariable String categoryId,
                                                                  @PathVariable Integer season) {
        try {
            matchDayService.deleteMatchDaysByCategoryIdAndSeason(categoryId, season);

//            leagueService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/categories/{categoryId}/seasons/{season}/number/{number}")
    public ResponseEntity<?> deleteMatchDayByCategoryIdSeasonAndNumber(@PathVariable String categoryId,
                                                                       @PathVariable Integer season,
                                                                       @PathVariable Integer number) {
        try {
            matchDayService.deleteMatchDayByCategoryIdSeasonAndNumber(categoryId, season, number);

//            leagueService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

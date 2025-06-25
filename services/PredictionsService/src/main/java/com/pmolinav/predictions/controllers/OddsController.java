package com.pmolinav.predictions.controllers;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.services.OddsService;
import com.pmolinav.predictionslib.dto.OddsDTO;
import com.pmolinav.predictionslib.model.Odds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("odds")
public class OddsController {

    @Autowired
    private final OddsService oddsService;

    @Autowired
    public OddsController(OddsService oddsService) {
        this.oddsService = oddsService;
    }

    @GetMapping
    public ResponseEntity<List<OddsDTO>> findAllOdds() {
        try {
            List<OddsDTO> oddsList = oddsService.findAllOdds();
            return ResponseEntity.ok(oddsList);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OddsDTO> findOddsById(@PathVariable Long id) {
        try {
            OddsDTO odds = oddsService.findOddsById(id);
            return ResponseEntity.ok(odds);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/events/{eventType}")
    public ResponseEntity<List<OddsDTO>> findOddsByEventType(@PathVariable String eventType) {
        try {
            List<OddsDTO> oddsList = oddsService.findOddsByEventType(eventType);
            return ResponseEntity.ok(oddsList);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Long> createOdds(@RequestBody OddsDTO oddsDTO) {
        try {
            Odds created = oddsService.createOdds(oddsDTO);
            return new ResponseEntity<>(created.getOddsId(), HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOdds(@PathVariable Long id, @RequestBody OddsDTO oddsDTO) {
        try {
            oddsService.updateOdds(id, oddsDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOdds(@PathVariable Long id) {
        try {
            oddsService.deleteOdds(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

// TODO: Complete
//    @PutMapping("{id}")
//    @Operation(summary = "Update a specific league", description = "Bearer token is required to authorize leagues.")
//    public ResponseEntity<Odds> updateOdds(@RequestParam String requestUid, @PathVariable long id, @RequestBody OddsDTO leaguesDetails) {
//        String message = validateMandatoryFieldsInRequest(leaguesDetails);
//        try {
//            Odds updatedOdds = leagueService.findById(id);
//
//            if (!StringUtils.hasText(message)) {
//                updatedOdds.setName(leagueDetails.getName());
//                updatedOdds.setDescription(leagueDetails.getDescription());
//                updatedOdds.setPrice(leagueDetails.getPrice());
//                if (leagueDetails.getType() != null) {
//                    updatedOdds.setType(leagueDetails.getType().name());
//                }
//                leagueService.createOdds(updatedOdds);
//                return ResponseEntity.ok(updatedOdds);
//            } else {
//                return ResponseEntity.badRequest().build();
//            }
//        } catch (NotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (UnexpectedException e) {
//            return ResponseEntity.status(e.getStatusCode()).build();
//        }
//    }

}

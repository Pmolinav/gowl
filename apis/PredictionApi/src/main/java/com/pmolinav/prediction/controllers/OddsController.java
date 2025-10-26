package com.pmolinav.prediction.controllers;

import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.prediction.exceptions.CustomStatusException;
import com.pmolinav.prediction.exceptions.NotFoundException;
import com.pmolinav.prediction.services.OddsService;
import com.pmolinav.predictionslib.dto.OddsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("odds")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "5. Odds", description = "The Odds Controller. Contains all the operations that can be performed on odds.")
public class OddsController {

    private static final Logger logger = LoggerFactory.getLogger(OddsController.class);

    @Autowired
    private OddsService oddsService;

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve odds by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<OddsDTO> findOddsById(@RequestParam String requestUid, @PathVariable Long id) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("OddsController: findOddsById. Path: id: {}", id);
            return ResponseEntity.ok(oddsService.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }

    @GetMapping("/events/{type}")
    @Operation(summary = "Retrieve odds by event ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<OddsDTO>> findOddsByEventType(@RequestParam String requestUid, @PathVariable String type) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("OddsController: findOddsByEventType. Path: type: {}", type);
            return ResponseEntity.ok(oddsService.findByEventType(type));
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "Get odds by match ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<OddsDTO>> findOddsByMatchId(@RequestParam String requestUid,
                                                           @PathVariable Long matchId) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("OddsController: findOddsByMatchId. Path: matchId: {}", matchId);
            List<OddsDTO> events = oddsService.findOddsByMatchId(matchId);
            return ResponseEntity.ok(events);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } finally {
            MDC.remove(MDCCommonKeys.REQUEST_UID.key());
        }
    }
}

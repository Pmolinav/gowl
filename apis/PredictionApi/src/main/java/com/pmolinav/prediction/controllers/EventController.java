package com.pmolinav.prediction.controllers;

import com.pmolinav.prediction.exceptions.CustomStatusException;
import com.pmolinav.prediction.exceptions.NotFoundException;
import com.pmolinav.prediction.services.EventService;
import com.pmolinav.predictionslib.dto.EventDTO;
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
@RequestMapping("events")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "6. Events", description = "The Event Controller. Contains all the operations that can be performed on events.")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/{type}")
    @Operation(summary = "Get a specific event by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<EventDTO> findEventById(@RequestParam String requestUid,
                                                  @PathVariable String type) {
        try {
            EventDTO event = eventService.findEventByEventType(type);
            return ResponseEntity.ok(event);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "Get events by match ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<EventDTO>> findEventsByMatchId(@RequestParam String requestUid,
                                                              @PathVariable Long matchId) {
        try {
            List<EventDTO> events = eventService.findEventsByMatchId(matchId);
            return ResponseEntity.ok(events);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}

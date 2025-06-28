package com.pmolinav.predictions.controllers;

import com.pmolinav.predictions.exceptions.CustomStatusException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.services.EventBOService;
import com.pmolinav.predictionslib.dto.EventDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("events")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "6. Events", description = "The Event Controller. Contains all the operations that can be performed on events.")
public class EventBOController {

    @Autowired
    private EventBOService eventBOService;

    @GetMapping
    @Operation(summary = "Retrieve all events", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<EventDTO>> findAllEvents(@RequestParam String requestUid) {
        try {
            List<EventDTO> events = eventBOService.findAllEvents();
            return ResponseEntity.ok(events);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/{type}")
    @Operation(summary = "Get a specific event by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<EventDTO> findEventByEventType(@RequestParam String requestUid,
                                                         @PathVariable String type) {
        try {
            EventDTO event = eventBOService.findEventByType(type);
            return ResponseEntity.ok(event);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "Get events by match ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<List<EventDTO>> findEventsByMatchId(@PathVariable Long matchId) {
        try {
            List<EventDTO> events = eventBOService.findEventsByMatchId(matchId);
            return ResponseEntity.ok(events);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new event", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> createEvent(@RequestParam String requestUid,
                                         @Valid @RequestBody EventDTO eventDTO,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        try {
            Long createdId = eventBOService.createEvent(eventDTO);
            return new ResponseEntity<>(createdId, HttpStatus.CREATED);
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @PutMapping("/{type}")
    @Operation(summary = "Update an event by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> updateEvent(@RequestParam String requestUid,
                                         @PathVariable String type,
                                         @Valid @RequestBody EventDTO eventDTO,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        try {
            eventBOService.updateEvent(type, eventDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/{type}")
    @Operation(summary = "Delete an event by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<?> deleteEvent(@RequestParam String requestUid,
                                         @PathVariable String type) {
        try {
            eventBOService.deleteEvent(type);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }


    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}

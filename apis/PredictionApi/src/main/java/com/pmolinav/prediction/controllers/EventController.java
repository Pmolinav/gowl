package com.pmolinav.prediction.controllers;

import com.pmolinav.auth.dto.MDCCommonKeys;
import com.pmolinav.prediction.exceptions.CustomStatusException;
import com.pmolinav.prediction.exceptions.NotFoundException;
import com.pmolinav.prediction.services.EventService;
import com.pmolinav.predictionslib.dto.EventDTO;
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

@AllArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("events")
@SecurityRequirement(name = "BearerToken")
@Tag(name = "6. Events", description = "The Event Controller. Contains all the operations that can be performed on events.")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @GetMapping("/{type}")
    @Operation(summary = "Get a specific event by ID", description = "Bearer token is required to authorize users.")
    public ResponseEntity<EventDTO> findEventById(@RequestParam String requestUid,
                                                  @PathVariable String type) {
        try {
            MDC.put(MDCCommonKeys.REQUEST_UID.key(), requestUid);
            logger.info("EventController: findEventById. Path: type: {}", type);
            EventDTO event = eventService.findEventByEventType(type);
            return ResponseEntity.ok(event);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CustomStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

}

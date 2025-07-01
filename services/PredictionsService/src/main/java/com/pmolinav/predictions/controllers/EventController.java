
package com.pmolinav.predictions.controllers;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.services.EventService;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> findAllEvents() {
        try {
            List<EventDTO> events = eventService.findAllEvents();
            return ResponseEntity.ok(events);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{type}")
    public ResponseEntity<EventDTO> findEventByEventType(@PathVariable String type) {
        try {
            EventDTO event = eventService.findEventByEventType(type);
            return ResponseEntity.ok(event);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody EventDTO eventDTO) {
        try {
            Event created = eventService.createEvent(eventDTO);
            return new ResponseEntity<>(created.getEventType(), HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{type}")
    public ResponseEntity<Void> updateEvent(@PathVariable String type, @RequestBody EventDTO eventDTO) {
        try {
            eventService.updateEvent(type, eventDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("{type}")
    public ResponseEntity<Void> deleteEventByEventType(@PathVariable String type) {
        try {
            eventService.deleteEventByEventType(type);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}

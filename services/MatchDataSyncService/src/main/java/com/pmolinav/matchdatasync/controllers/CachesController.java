package com.pmolinav.matchdatasync.controllers;

import com.pmolinav.matchdatasync.services.EventService;
import com.pmolinav.matchdatasync.services.ExternalCategoryMappingService;
import com.pmolinav.predictionslib.model.Event;
import com.pmolinav.predictionslib.model.ExternalCategoryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("caches")
public class CachesController {

    private final ExternalCategoryMappingService mappingService;
    private final EventService eventService;

    public CachesController(ExternalCategoryMappingService mappingService,
                            EventService eventService) {
        this.mappingService = mappingService;
        this.eventService = eventService;
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getExternalCategoryCache(@PathVariable String categoryId,
                                                      @RequestParam(required = false) Boolean onlyCache) {
        ExternalCategoryMapping mapping;
        if (Boolean.TRUE.equals(onlyCache)) {
            mapping = mappingService.getCachedOnly(categoryId);
        } else {
            mapping = mappingService.cachedFindById(categoryId);
        }
        return mapping != null ? ResponseEntity.ok(mapping) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/categories")
    public ResponseEntity<Void> invalidateExternalCategoryCache(@RequestParam(required = false) String categoryId) {
        if (categoryId != null && !categoryId.isBlank()) {
            mappingService.evictCategoryCache(categoryId);
        } else {
            mappingService.clearAllCategoryCache();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/events/{type}")
    public ResponseEntity<?> getEventCache(@PathVariable String type,
                                           @RequestParam(required = false) Boolean onlyCache) {
        Event event;
        if (Boolean.TRUE.equals(onlyCache)) {
            event = eventService.getCachedOnly(type);
        } else {
            event = eventService.cachedFindEventByType(type);
        }
        return event != null ? ResponseEntity.ok(event) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/events")
    public ResponseEntity<Void> invalidateEventCache(@RequestParam(required = false) String type) {
        if (type != null && !type.isBlank()) {
            eventService.evictEventCache(type);
        } else {
            eventService.clearAllEventCache();
        }
        return ResponseEntity.ok().build();
    }
}

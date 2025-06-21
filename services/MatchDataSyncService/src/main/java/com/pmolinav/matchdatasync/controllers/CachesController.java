package com.pmolinav.matchdatasync.controllers;

import com.pmolinav.matchdatasync.services.ExternalCategoryMappingService;
import com.pmolinav.predictionslib.model.ExternalCategoryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("caches")
public class CachesController {

    private final ExternalCategoryMappingService mappingService;

    public CachesController(ExternalCategoryMappingService mappingService) {
        this.mappingService = mappingService;
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
            mappingService.clearAllCache();
        }
        return ResponseEntity.ok().build();
    }
}

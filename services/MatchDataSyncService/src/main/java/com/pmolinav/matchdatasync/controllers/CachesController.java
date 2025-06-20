package com.pmolinav.matchdatasync.controllers;

import com.pmolinav.matchdatasync.services.ExternalCategoryMappingService;
import org.springframework.beans.factory.annotation.Autowired;
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

package com.pmolinav.matchdatasync.services;

import com.pmolinav.matchdatasync.exceptions.InternalServerErrorException;
import com.pmolinav.matchdatasync.exceptions.NotFoundException;
import com.pmolinav.matchdatasync.repositories.ExternalCategoryMappingRepository;
import com.pmolinav.predictionslib.model.ExternalCategoryMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ExternalCategoryMappingService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalCategoryMappingService.class);

    private final ExternalCategoryMappingRepository externalCategoryMappingRepository;
    private final CacheManager cacheManager;

    public ExternalCategoryMappingService(ExternalCategoryMappingRepository externalCategoryMappingRepository,
                                          CacheManager cacheManager) {
        this.externalCategoryMappingRepository = externalCategoryMappingRepository;
        this.cacheManager = cacheManager;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "externalSportKeyCache", key = "#categoryId")
    public ExternalCategoryMapping cachedFindById(String categoryId) {
        Optional<ExternalCategoryMapping> sportKeyMapping;
        try {
            sportKeyMapping = externalCategoryMappingRepository.findById(categoryId);
        } catch (Exception e) {
            logger.error("Unexpected error while searching all matches in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (sportKeyMapping.isEmpty()) {
            throw new NotFoundException("External sport key for category ID " + categoryId + " not found");
        }
        return sportKeyMapping.get();
    }

    // Returns valid value if exists in cache or null if not.
    public ExternalCategoryMapping getCachedOnly(String categoryId) {
        Cache cache = cacheManager.getCache("externalSportKeyCache");
        if (cache == null) return null;

        Cache.ValueWrapper valueWrapper = cache.get(categoryId);
        if (valueWrapper == null) return null;

        return (ExternalCategoryMapping) valueWrapper.get();
    }

    @CacheEvict(value = "externalSportKeyCache", key = "#categoryId")
    public void evictCategoryCache(String categoryId) {
        // No operation. Manual invalidation by categoryId.
        logger.debug("Categories cache (externalSportKeyCache) was invalidated by categoryId {}.", categoryId);
    }

    @CacheEvict(value = "externalSportKeyCache", allEntries = true)
    public void clearAllCache() {
        // No operation. Manual invalidation for all entries.
        logger.debug("Categories cache (externalSportKeyCache) was completely invalidated.");
    }
}

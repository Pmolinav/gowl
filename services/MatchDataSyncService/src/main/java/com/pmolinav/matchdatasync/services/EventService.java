package com.pmolinav.matchdatasync.services;

import com.pmolinav.matchdatasync.exceptions.InternalServerErrorException;
import com.pmolinav.matchdatasync.exceptions.NotFoundException;
import com.pmolinav.matchdatasync.repositories.EventRepository;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.mapper.EventMapper;
import com.pmolinav.predictionslib.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@EnableAsync
@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CacheManager cacheManager;

    @Autowired
    public EventService(EventRepository eventRepository, EventMapper eventMapper, CacheManager cacheManager) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.cacheManager = cacheManager;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "eventKeyCache", key = "#type")
    public Event cachedFindEventByType(String type) {
        try {
            return eventRepository.findById(type)
                    .orElseThrow(() -> new NotFoundException("Event not found with type: " + type));
        } catch (NotFoundException e) {
            logger.error("Event not found: {}", type);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving event with type {}", type, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    // Returns valid value if exists in cache or null if not.
    public Event getCachedOnly(String type) {
        Cache cache = cacheManager.getCache("eventKeyCache");
        if (cache == null) return null;

        Cache.ValueWrapper valueWrapper = cache.get(type);
        if (valueWrapper == null) return null;

        return (Event) valueWrapper.get();
    }

    @CacheEvict(value = "eventKeyCache", key = "#type")
    public void evictEventCache(String type) {
        // No operation. Manual invalidation by type.
        logger.debug("Events cache (eventKeyCache) was invalidated by type {}.", type);
    }

    @CacheEvict(value = "eventKeyCache", allEntries = true)
    public void clearAllEventCache() {
        // No operation. Manual invalidation for all entries.
        logger.debug("Events cache (eventKeyCache) was completely invalidated.");
    }

    @Transactional
    public Event createEvent(EventDTO eventDTO) {
        try {
            Event event = eventMapper.eventDtoToEntity(eventDTO);
            return eventRepository.save(event);
        } catch (Exception e) {
            logger.error("Error creating event", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}

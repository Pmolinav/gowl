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
import org.springframework.util.CollectionUtils;

import java.util.List;

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
    @Cacheable(value = "eventKeyCache", key = "#eventType")
    public Event cachedFindEventByType(String eventType) {
        try {
            return eventRepository.findById(eventType)
                    .orElseThrow(() -> new NotFoundException("Event not found with eventType: " + eventType));
        } catch (NotFoundException e) {
            logger.error("Event not found: {}", eventType);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving event with eventType {}", eventType, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Event> findByMatchId(Long matchId) {
        try {
            List<Event> events = eventRepository.findByMatchId(matchId);
            if (CollectionUtils.isEmpty(events)) {
                logger.warn("No events found by matchId {}", matchId);
                throw new NotFoundException("Events not found for matchId " + matchId);
            }
            return events;
        } catch (Exception e) {
            logger.error("Error retrieving events by matchId {}", matchId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    // Returns valid value if exists in cache or null if not.
    public Event getCachedOnly(String eventType) {
        Cache cache = cacheManager.getCache("eventKeyCache");
        if (cache == null) return null;

        Cache.ValueWrapper valueWrapper = cache.get(eventType);
        if (valueWrapper == null) return null;

        return (Event) valueWrapper.get();
    }

    @CacheEvict(value = "eventKeyCache", key = "#eventType")
    public void evictEventCache(String eventType) {
        // No operation. Manual invalidation by eventType.
        logger.debug("Events cache (eventKeyCache) was invalidated by eventType {}.", eventType);
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

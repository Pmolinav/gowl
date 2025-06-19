package com.pmolinav.predictions.services;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.repositories.EventRepository;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.mapper.EventMapper;
import com.pmolinav.predictionslib.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
//    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
//        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<EventDTO> findAllEvents() {
        List<Event> events;
        try {
            events = eventRepository.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving all events", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(events)) {
            logger.warn("No events found");
            throw new NotFoundException("No events found");
        }
        return events.stream().map(eventMapper::eventEntityToDto).toList();
    }

    @Transactional(readOnly = true)
    public EventDTO findEventById(Long id) {
        try {
            Event event = eventRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
            return eventMapper.eventEntityToDto(event);
        } catch (NotFoundException e) {
            logger.error("Event not found: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving event with id {}", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<EventDTO> findByMatchId(Long matchId) {
        List<Event> events;
        try {
            events = eventRepository.findByMatchId(matchId);
        } catch (Exception e) {
            logger.error("Error retrieving events by matchId {}", matchId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(events)) {
            logger.warn("No events found by matchId {}", matchId);
            throw new NotFoundException("Events not found for matchId " + matchId);
        }
        return events.stream().map(eventMapper::eventEntityToDto).toList();
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

    @Transactional
    public void updateEvent(Long eventId, EventDTO eventDTO) {
        try {
            Event existing = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException("Event with id " + eventId + " not found."));

            Event updated = eventMapper.eventDtoToEntity(eventDTO);
            updated.setEventId(existing.getEventId()); // ensure ID is preserved

            eventRepository.save(updated);
        } catch (NotFoundException e) {
            logger.error("Event not found with id: {}", eventId);
            throw e;
        } catch (Exception e) {
            logger.error("Error while updating event with id {}", eventId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteEventById(Long id) {
        try {
            Event event = eventRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
            eventRepository.delete(event);
        } catch (NotFoundException e) {
            logger.error("Event not found with id: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting event by id {}", id, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteEventsByMatchId(Long matchId) {
        try {
            List<Event> events = eventRepository.findByMatchId(matchId);
            if (CollectionUtils.isEmpty(events)) {
                throw new NotFoundException("No events found for matchId: " + matchId);
            }
            eventRepository.deleteAll(events);
        } catch (Exception e) {
            logger.error("Error deleting events by matchId {}", matchId, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

//    @Async
//    public void storeInKafka(ChangeType changeType, Long eventId, Event event) {
//        try {
//            messageProducer.sendMessage(this.KAFKA_TOPIC, new History(
//                    new Date(),
//                    changeType,
//                    "Event",
//                    String.valueOf(eventId),
//                    event == null ? null : new ObjectMapper().writeValueAsString(event), // TODO: USE JSON PATCH.
//                    "Admin" // TODO: createEvent is not implemented yet.
//            ));
//        } catch (Exception e) {
//            logger.warn("Kafka operation {} with name {} and event {} need to be reviewed", changeType, eventId, event);
//        }
//    }
}

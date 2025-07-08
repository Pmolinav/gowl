package com.pmolinav.predictions.unit;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.dto.EventType;
import com.pmolinav.predictionslib.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventControllerTest extends BaseUnitTest {

    EventDTO eventDTO;
    List<EventDTO> expectedEvents;
    ResponseEntity<?> result;

    /* FIND ALL EVENTS */
    @Test
    void findAllEventsHappyPath() {
        when(eventServiceMock.findAllEvents()).thenReturn(expectedEvents());
        result = eventController.findAllEvents();
        verify(eventServiceMock).findAllEvents();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedEvents(), result.getBody());
    }

    @Test
    void findAllEventsNotFound() {
        when(eventServiceMock.findAllEvents()).thenThrow(new NotFoundException("Not Found"));
        result = eventController.findAllEvents();
        verify(eventServiceMock).findAllEvents();
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findAllEventsServerError() {
        when(eventServiceMock.findAllEvents()).thenThrow(new InternalServerErrorException("Internal Error"));
        result = eventController.findAllEvents();
        verify(eventServiceMock).findAllEvents();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* CREATE EVENT */
    @Test
    void createEventHappyPath() {
        Event event = new Event();
        event.setEventType(EventType.H2H.getName());
        when(eventServiceMock.createEvent(any(EventDTO.class))).thenReturn(event);

        eventDTO = new EventDTO();
        result = eventController.createEvent(eventDTO);

        verify(eventServiceMock).createEvent(any(EventDTO.class));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(EventType.H2H.getName(), result.getBody());
    }

    @Test
    void createEventServerError() {
        eventDTO = new EventDTO();
        when(eventServiceMock.createEvent(any(EventDTO.class))).thenThrow(new InternalServerErrorException("Error"));

        result = eventController.createEvent(eventDTO);

        verify(eventServiceMock).createEvent(any(EventDTO.class));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* FIND EVENT BY TYPE */
    @Test
    void findEventByTypeHappyPath() {
        EventDTO dto = new EventDTO();
        when(eventServiceMock.findEventByEventType(EventType.H2H.getName())).thenReturn(dto);

        result = eventController.findEventByEventType(EventType.H2H.getName());

        verify(eventServiceMock).findEventByEventType(EventType.H2H.getName());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dto, result.getBody());
    }

    @Test
    void findEventByTypeNotFound() {
        when(eventServiceMock.findEventByEventType(EventType.H2H.getName())).thenThrow(new NotFoundException("Not found"));

        result = eventController.findEventByEventType(EventType.H2H.getName());

        verify(eventServiceMock).findEventByEventType(EventType.H2H.getName());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findEventByTypeServerError() {
        when(eventServiceMock.findEventByEventType(EventType.H2H.getName())).thenThrow(new InternalServerErrorException("Error"));

        result = eventController.findEventByEventType(EventType.H2H.getName());

        verify(eventServiceMock).findEventByEventType(EventType.H2H.getName());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* DELETE EVENT BY ID */
    @Test
    void deleteEventByEventTypeHappyPath() {
        doNothing().when(eventServiceMock).deleteEventByEventType(EventType.H2H.getName());

        result = eventController.deleteEventByEventType(EventType.H2H.getName());

        verify(eventServiceMock).deleteEventByEventType(EventType.H2H.getName());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteEventByEventTypeNotFound() {
        doThrow(new NotFoundException("Not found"))
                .when(eventServiceMock).deleteEventByEventType(EventType.H2H.getName());

        result = eventController.deleteEventByEventType(EventType.H2H.getName());

        verify(eventServiceMock).deleteEventByEventType(EventType.H2H.getName());
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void deleteEventByEventTypeServerError() {
        doThrow(new InternalServerErrorException("Error"))
                .when(eventServiceMock).deleteEventByEventType(EventType.H2H.getName());

        result = eventController.deleteEventByEventType(EventType.H2H.getName());

        verify(eventServiceMock).deleteEventByEventType(EventType.H2H.getName());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* Helpers */
    private List<EventDTO> expectedEvents() {
        EventDTO e1 = new EventDTO();
        e1.setEventType(EventType.H2H.getName());
        EventDTO e2 = new EventDTO();
        e2.setEventType(EventType.TOTALS.getName());
        return List.of(e1, e2);
    }

}

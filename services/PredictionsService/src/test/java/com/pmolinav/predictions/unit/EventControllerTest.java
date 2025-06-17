package com.pmolinav.predictions.unit;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.EventDTO;
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
        event.setEventId(1L);
        when(eventServiceMock.createEvent(any(EventDTO.class))).thenReturn(event);

        eventDTO = new EventDTO();
        result = eventController.createEvent(eventDTO);

        verify(eventServiceMock).createEvent(any(EventDTO.class));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(1L, result.getBody());
    }

    @Test
    void createEventServerError() {
        eventDTO = new EventDTO();
        when(eventServiceMock.createEvent(any(EventDTO.class))).thenThrow(new InternalServerErrorException("Error"));

        result = eventController.createEvent(eventDTO);

        verify(eventServiceMock).createEvent(any(EventDTO.class));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* FIND EVENT BY ID */
    @Test
    void findEventByIdHappyPath() {
        EventDTO dto = new EventDTO();
        when(eventServiceMock.findEventById(1L)).thenReturn(dto);

        result = eventController.findEventById(1L);

        verify(eventServiceMock).findEventById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dto, result.getBody());
    }

    @Test
    void findEventByIdNotFound() {
        when(eventServiceMock.findEventById(1L)).thenThrow(new NotFoundException("Not found"));

        result = eventController.findEventById(1L);

        verify(eventServiceMock).findEventById(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findEventByIdServerError() {
        when(eventServiceMock.findEventById(1L)).thenThrow(new InternalServerErrorException("Error"));

        result = eventController.findEventById(1L);

        verify(eventServiceMock).findEventById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* FIND EVENT BY MATCH ID */
    @Test
    void findEventByMatchIdHappyPath() {
        List<EventDTO> dtoList = List.of(new EventDTO());
        when(eventServiceMock.findByMatchId(1L)).thenReturn(dtoList);

        result = eventController.findEventsByMatchId(1L);

        verify(eventServiceMock).findByMatchId(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dtoList, result.getBody());
    }

    @Test
    void findEventByMatchIdNotFound() {
        when(eventServiceMock.findByMatchId(1L)).thenThrow(new NotFoundException("Not found"));

        result = eventController.findEventsByMatchId(1L);

        verify(eventServiceMock).findByMatchId(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findEventByMatchIdServerError() {
        when(eventServiceMock.findByMatchId(1L)).thenThrow(new InternalServerErrorException("Error"));

        result = eventController.findEventsByMatchId(1L);

        verify(eventServiceMock).findByMatchId(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* DELETE EVENT BY ID */
    @Test
    void deleteEventByIdHappyPath() {
        doNothing().when(eventServiceMock).deleteEventById(1L);

        result = eventController.deleteEventById(1L);

        verify(eventServiceMock).deleteEventById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteEventByIdNotFound() {
        doThrow(new NotFoundException("Not found")).when(eventServiceMock).deleteEventById(1L);

        result = eventController.deleteEventById(1L);

        verify(eventServiceMock).deleteEventById(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void deleteEventByIdServerError() {
        doThrow(new InternalServerErrorException("Error")).when(eventServiceMock).deleteEventById(1L);

        result = eventController.deleteEventById(1L);

        verify(eventServiceMock).deleteEventById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* Helpers */
    private List<EventDTO> expectedEvents() {
        EventDTO e1 = new EventDTO();
        e1.setName("Event 1");
        EventDTO e2 = new EventDTO();
        e2.setName("Event 2");
        return List.of(e1, e2);
    }

}

package com.pmolinav.prediction.units;

import com.pmolinav.prediction.exceptions.InternalServerErrorException;
import com.pmolinav.prediction.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.dto.EventType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EventControllerTest extends BaseUnitTest {

    private EventDTO eventDTO;
    private List<EventDTO> expectedEvents;
    private ResponseEntity<?> result;

    /* FIND EVENT BY ID */
    @Test
    void findEventByIdHappyPath() {
        whenFindEventByIdInServiceReturnedValidEvent();
        andFindEventByIdIsCalledInController();
        thenVerifyFindEventByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseIs(eventDTO);
    }

    @Test
    void findEventByIdNotFound() {
        whenFindEventByIdInServiceThrowsNotFoundException();
        andFindEventByIdIsCalledInController();
        thenVerifyFindEventByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findEventByIdServerError() {
        whenFindEventByIdInServiceThrowsServerException();
        andFindEventByIdIsCalledInController();
        thenVerifyFindEventByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND EVENTS BY MATCH ID */
    @Test
    void findEventByMatchIdHappyPath() {
        whenFindEventByMatchIdInServiceReturnedValidEvent();
        andFindEventByMatchIdIsCalledInController();
        thenVerifyFindEventByMatchIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedEvents);
    }

    @Test
    void findEventByMatchIdNotFound() {
        whenFindEventByMatchIdInServiceThrowsNotFoundException();
        andFindEventByMatchIdIsCalledInController();
        thenVerifyFindEventByMatchIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findEventByMatchIdServerError() {
        whenFindEventByMatchIdInServiceThrowsServerException();
        andFindEventByMatchIdIsCalledInController();
        thenVerifyFindEventByMatchIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- SETUP MOCK RETURNS ---

    private void whenFindEventByIdInServiceReturnedValidEvent() {
        eventDTO = new EventDTO(EventType.H2H.getName(), 2L, EventType.H2H.getDescription());
        when(eventServiceMock.findEventById(1L)).thenReturn(eventDTO);
    }

    private void whenFindEventByIdInServiceThrowsNotFoundException() {
        when(eventServiceMock.findEventById(1L)).thenThrow(new NotFoundException("Event not found"));
    }

    private void whenFindEventByIdInServiceThrowsServerException() {
        when(eventServiceMock.findEventById(1L)).thenThrow(new InternalServerErrorException("Server error"));
    }

    private void whenFindEventByMatchIdInServiceReturnedValidEvent() {
        expectedEvents = List.of(
                new EventDTO(EventType.H2H.getName(), 2L, EventType.H2H.getDescription())
        );
        when(eventServiceMock.findEventsByMatchId(1L)).thenReturn(expectedEvents);
    }

    private void whenFindEventByMatchIdInServiceThrowsNotFoundException() {
        when(eventServiceMock.findEventsByMatchId(1L)).thenThrow(new NotFoundException("Event not found"));
    }

    private void whenFindEventByMatchIdInServiceThrowsServerException() {
        when(eventServiceMock.findEventsByMatchId(1L)).thenThrow(new InternalServerErrorException("Server error"));
    }

    // --- CALL CONTROLLER METHODS ---

    private void andFindEventByIdIsCalledInController() {
        result = eventController.findEventById(requestUid, 1L);
    }

    private void andFindEventByMatchIdIsCalledInController() {
        result = eventController.findEventsByMatchId(requestUid, 1L);
    }

    // --- VERIFY SERVICE METHOD CALLS ---

    private void thenVerifyFindEventByIdHasBeenCalledInService() {
        verify(eventServiceMock, times(1)).findEventById(anyLong());
    }

    private void thenVerifyFindEventByMatchIdHasBeenCalledInService() {
        verify(eventServiceMock, times(1)).findEventsByMatchId(anyLong());
    }

    // --- ASSERTIONS ---

    private void thenReceivedStatusCodeIs(HttpStatus expectedStatus) {
        assertEquals(expectedStatus, result.getStatusCode());
    }

    private void thenReceivedResponseIs(EventDTO expectedResponse) {
        assertNotNull(result);
        assertEquals(expectedResponse, result.getBody());
    }

    private void thenReceivedResponseListIs(List<EventDTO> expectedResponse) {
        assertNotNull(result);
        assertEquals(expectedResponse, result.getBody());
    }
}

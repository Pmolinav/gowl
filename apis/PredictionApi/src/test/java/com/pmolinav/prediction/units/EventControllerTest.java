package com.pmolinav.prediction.units;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
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

    // --- SETUP MOCK RETURNS ---

    private void whenFindEventByIdInServiceReturnedValidEvent() {
        eventDTO = new EventDTO(EventType.H2H.getName(), EventType.H2H.getDescription());
        when(eventServiceMock.findEventByEventType(EventType.H2H.getName())).thenReturn(eventDTO);
    }

    private void whenFindEventByIdInServiceThrowsNotFoundException() {
        when(eventServiceMock.findEventByEventType(EventType.H2H.getName())).thenThrow(new NotFoundException("Event not found"));
    }

    private void whenFindEventByIdInServiceThrowsServerException() {
        when(eventServiceMock.findEventByEventType(EventType.H2H.getName())).thenThrow(new InternalServerErrorException("Server error"));
    }


    // --- CALL CONTROLLER METHODS ---

    private void andFindEventByIdIsCalledInController() {
        result = eventController.findEventById(requestUid, EventType.H2H.getName());
    }

    // --- VERIFY SERVICE METHOD CALLS ---

    private void thenVerifyFindEventByIdHasBeenCalledInService() {
        verify(eventServiceMock, times(1)).findEventByEventType(anyString());
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

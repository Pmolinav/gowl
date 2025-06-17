package com.pmolinav.prediction.units;

import com.pmolinav.prediction.exceptions.InternalServerErrorException;
import com.pmolinav.prediction.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.EventDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventControllerTest extends BaseUnitTest {

    private EventDTO eventDTO;
    private List<EventDTO> expectedEvents;
    private ResponseEntity<?> result;

    /* FIND ALL EVENTS */
    @Test
    void findAllEventsHappyPath() {
        whenFindAllEventsInServiceReturnedValidEvents();
        andFindAllEventsIsCalledInController();
        thenVerifyFindAllEventsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedEvents);
    }

    @Test
    void findAllEventsNotFound() {
        whenFindAllEventsInServiceThrowsNotFoundException();
        andFindAllEventsIsCalledInController();
        thenVerifyFindAllEventsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllEventsServerError() {
        whenFindAllEventsInServiceThrowsServerException();
        andFindAllEventsIsCalledInController();
        thenVerifyFindAllEventsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

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

    /* CREATE EVENT */
    @Test
    void createEventHappyPath() {
        givenValidEventDTOForRequest();
        whenCreateEventInServiceReturnedValidId();
        andCreateEventIsCalledInController();
        thenVerifyCreateEventHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseLongIs(1L);
    }

    @Test
    void createEventBadRequest() {
        givenValidEventDTOForRequest();
        andCreateEventIsCalledInControllerWithBindingResultErrors();
        thenReceivedStatusCodeIs(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createEventServerError() {
        givenValidEventDTOForRequest();
        whenCreateEventInServiceThrowsServerException();
        andCreateEventIsCalledInController();
        thenVerifyCreateEventHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* UPDATE EVENT */
    @Test
    void updateEventHappyPath() {
        givenValidEventDTOForRequest();
        whenUpdateEventInServiceDoesNothing();
        andUpdateEventIsCalledInController();
        thenVerifyUpdateEventHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void updateEventNotFound() {
        givenValidEventDTOForRequest();
        whenUpdateEventInServiceThrowsNotFoundException();
        andUpdateEventIsCalledInController();
        thenVerifyUpdateEventHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateEventServerError() {
        givenValidEventDTOForRequest();
        whenUpdateEventInServiceThrowsServerException();
        andUpdateEventIsCalledInController();
        thenVerifyUpdateEventHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE EVENT */
    @Test
    void deleteEventHappyPath() {
        whenDeleteEventInServiceDoesNothing();
        andDeleteEventIsCalledInController();
        thenVerifyDeleteEventHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteEventNotFound() {
        whenDeleteEventInServiceThrowsNotFoundException();
        andDeleteEventIsCalledInController();
        thenVerifyDeleteEventHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteEventServerError() {
        whenDeleteEventInServiceThrowsServerException();
        andDeleteEventIsCalledInController();
        thenVerifyDeleteEventHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- SETUP MOCK RETURNS ---

    private void givenValidEventDTOForRequest() {
        eventDTO = new EventDTO();
        eventDTO.setEventId(1L);
        eventDTO.setMatchId(2L);
        eventDTO.setName("Event Test");
        // set other properties as needed
    }

    private void whenFindAllEventsInServiceReturnedValidEvents() {
        expectedEvents = List.of(
                new EventDTO(1L, 2L, "Event 1", "Event description"),
                new EventDTO(2L, 2L, "Event 2", "Event description 2")
        );
        when(eventServiceMock.findAllEvents()).thenReturn(expectedEvents);
    }

    private void whenFindAllEventsInServiceThrowsNotFoundException() {
        when(eventServiceMock.findAllEvents()).thenThrow(new NotFoundException("Events not found"));
    }

    private void whenFindAllEventsInServiceThrowsServerException() {
        when(eventServiceMock.findAllEvents()).thenThrow(new InternalServerErrorException("Server error"));
    }

    private void whenFindEventByIdInServiceReturnedValidEvent() {
        eventDTO = new EventDTO(1L, 2L, "Event Test", "Some description");
        when(eventServiceMock.findEventById(1L)).thenReturn(eventDTO);
    }

    private void whenFindEventByIdInServiceThrowsNotFoundException() {
        when(eventServiceMock.findEventById(1L)).thenThrow(new NotFoundException("Event not found"));
    }

    private void whenFindEventByIdInServiceThrowsServerException() {
        when(eventServiceMock.findEventById(1L)).thenThrow(new InternalServerErrorException("Server error"));
    }

    private void whenCreateEventInServiceReturnedValidId() {
        when(eventServiceMock.createEvent(any(EventDTO.class))).thenReturn(1L);
    }

    private void whenCreateEventInServiceThrowsServerException() {
        when(eventServiceMock.createEvent(any(EventDTO.class)))
                .thenThrow(new InternalServerErrorException("Server error"));
    }

    private void whenUpdateEventInServiceDoesNothing() {
        doNothing().when(eventServiceMock).updateEvent(eq(1L), any(EventDTO.class));
    }

    private void whenUpdateEventInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Event not found")).when(eventServiceMock).updateEvent(eq(1L), any(EventDTO.class));
    }

    private void whenUpdateEventInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Server error")).when(eventServiceMock).updateEvent(eq(1L), any(EventDTO.class));
    }

    private void whenDeleteEventInServiceDoesNothing() {
        doNothing().when(eventServiceMock).deleteEvent(1L);
    }

    private void whenDeleteEventInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Event not found")).when(eventServiceMock).deleteEvent(1L);
    }

    private void whenDeleteEventInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Server error")).when(eventServiceMock).deleteEvent(1L);
    }

    // --- CALL CONTROLLER METHODS ---

    private void andFindAllEventsIsCalledInController() {
        result = eventController.findAllEvents(requestUid);
    }

    private void andFindEventByIdIsCalledInController() {
        result = eventController.findEventById(requestUid, 1L);
    }

    private void andCreateEventIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = eventController.createEvent(requestUid, eventDTO, bindingResult);
    }

    private void andCreateEventIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("eventDTO", "name", "Name is mandatory")
        ));
        result = eventController.createEvent(requestUid, eventDTO, bindingResult);
    }

    private void andUpdateEventIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = eventController.updateEvent(requestUid, 1L, eventDTO, bindingResult);
    }

    private void andUpdateEventIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("eventDTO", "name", "Name is mandatory")
        ));
        result = eventController.updateEvent(requestUid, 1L, eventDTO, bindingResult);
    }

    private void andDeleteEventIsCalledInController() {
        result = eventController.deleteEvent(requestUid, 1L);
    }

    // --- VERIFY SERVICE METHOD CALLS ---

    private void thenVerifyFindAllEventsHasBeenCalledInService() {
        verify(eventServiceMock, times(1)).findAllEvents();
    }

    private void thenVerifyFindEventByIdHasBeenCalledInService() {
        verify(eventServiceMock, times(1)).findEventById(anyLong());
    }

    private void thenVerifyCreateEventHasBeenCalledInService() {
        verify(eventServiceMock, times(1)).createEvent(any(EventDTO.class));
    }

    private void thenVerifyUpdateEventHasBeenCalledInService() {
        verify(eventServiceMock, times(1)).updateEvent(anyLong(), any(EventDTO.class));
    }

    private void thenVerifyDeleteEventHasBeenCalledInService() {
        verify(eventServiceMock, times(1)).deleteEvent(anyLong());
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

    private void thenReceivedResponseLongIs(Long expectedResponse) {
        assertNotNull(result);
        assertEquals(expectedResponse, result.getBody());
    }
}

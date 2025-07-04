package com.pmolinav.predictions.units;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.dto.EventType;
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

class EventBOControllerTest extends BaseUnitTest {


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
    void findEventByEventTypeHappyPath() {
        whenFindEventByIdInServiceReturnedValidEvent();
        andFindEventByEventTypeIsCalledInController();
        thenVerifyFindEventByEventTypeHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseIs(eventDTO);
    }

    @Test
    void findEventByEventTypeNotFound() {
        whenFindEventByEventTypeInServiceThrowsNotFoundException();
        andFindEventByEventTypeIsCalledInController();
        thenVerifyFindEventByEventTypeHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findEventByEventTypeServerError() {
        whenFindEventByEventTypeInServiceThrowsServerException();
        andFindEventByEventTypeIsCalledInController();
        thenVerifyFindEventByEventTypeHasBeenCalledInService();
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
        eventDTO.setEventType(EventType.H2H.getName());
        // set other properties as needed
    }

    private void whenFindAllEventsInServiceReturnedValidEvents() {
        expectedEvents = List.of(
                new EventDTO(EventType.H2H.getName(), EventType.H2H.getDescription()),
                new EventDTO(EventType.H2H.getName(), EventType.H2H.getDescription())
        );
        when(eventBOServiceMock.findAllEvents()).thenReturn(expectedEvents);
    }

    private void whenFindAllEventsInServiceThrowsNotFoundException() {
        when(eventBOServiceMock.findAllEvents()).thenThrow(new NotFoundException("Events not found"));
    }

    private void whenFindAllEventsInServiceThrowsServerException() {
        when(eventBOServiceMock.findAllEvents()).thenThrow(new InternalServerErrorException("Server error"));
    }

    private void whenFindEventByIdInServiceReturnedValidEvent() {
        eventDTO = new EventDTO(EventType.H2H.getName(), EventType.H2H.getDescription());
        when(eventBOServiceMock.findEventByType(EventType.H2H.getName())).thenReturn(eventDTO);
    }

    private void whenFindEventByEventTypeInServiceThrowsNotFoundException() {
        when(eventBOServiceMock.findEventByType(EventType.H2H.getName())).thenThrow(new NotFoundException("Event not found"));
    }

    private void whenFindEventByEventTypeInServiceThrowsServerException() {
        when(eventBOServiceMock.findEventByType(EventType.H2H.getName())).thenThrow(new InternalServerErrorException("Server error"));
    }

    private void whenCreateEventInServiceReturnedValidId() {
        when(eventBOServiceMock.createEvent(any(EventDTO.class))).thenReturn(1L);
    }

    private void whenCreateEventInServiceThrowsServerException() {
        when(eventBOServiceMock.createEvent(any(EventDTO.class)))
                .thenThrow(new InternalServerErrorException("Server error"));
    }

    private void whenUpdateEventInServiceDoesNothing() {
        doNothing().when(eventBOServiceMock).updateEvent(eq(EventType.H2H.getName()), any(EventDTO.class));
    }

    private void whenUpdateEventInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Event not found")).when(eventBOServiceMock).updateEvent(eq(EventType.H2H.getName()), any(EventDTO.class));
    }

    private void whenUpdateEventInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Server error")).when(eventBOServiceMock).updateEvent(eq(EventType.H2H.getName()), any(EventDTO.class));
    }

    private void whenDeleteEventInServiceDoesNothing() {
        doNothing().when(eventBOServiceMock).deleteEvent(EventType.H2H.getName());
    }

    private void whenDeleteEventInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Event not found")).when(eventBOServiceMock).deleteEvent(EventType.H2H.getName());
    }

    private void whenDeleteEventInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Server error")).when(eventBOServiceMock).deleteEvent(EventType.H2H.getName());
    }

    // --- CALL CONTROLLER METHODS ---

    private void andFindAllEventsIsCalledInController() {
        result = eventBOController.findAllEvents(requestUid);
    }

    private void andFindEventByEventTypeIsCalledInController() {
        result = eventBOController.findEventByEventType(requestUid, EventType.H2H.getName());
    }

    private void andCreateEventIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = eventBOController.createEvent(requestUid, eventDTO, bindingResult);
    }

    private void andCreateEventIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("eventDTO", "name", "Name is mandatory")
        ));
        result = eventBOController.createEvent(requestUid, eventDTO, bindingResult);
    }

    private void andUpdateEventIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = eventBOController.updateEvent(requestUid, EventType.H2H.getName(), eventDTO, bindingResult);
    }

    private void andUpdateEventIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("eventDTO", "name", "Name is mandatory")
        ));
        result = eventBOController.updateEvent(requestUid, EventType.H2H.getName(), eventDTO, bindingResult);
    }

    private void andDeleteEventIsCalledInController() {
        result = eventBOController.deleteEvent(requestUid, EventType.H2H.getName());
    }

    // --- VERIFY SERVICE METHOD CALLS ---

    private void thenVerifyFindAllEventsHasBeenCalledInService() {
        verify(eventBOServiceMock, times(1)).findAllEvents();
    }

    private void thenVerifyFindEventByEventTypeHasBeenCalledInService() {
        verify(eventBOServiceMock, times(1)).findEventByType(anyString());
    }

    private void thenVerifyCreateEventHasBeenCalledInService() {
        verify(eventBOServiceMock, times(1)).createEvent(any(EventDTO.class));
    }

    private void thenVerifyUpdateEventHasBeenCalledInService() {
        verify(eventBOServiceMock, times(1)).updateEvent(anyString(), any(EventDTO.class));
    }

    private void thenVerifyDeleteEventHasBeenCalledInService() {
        verify(eventBOServiceMock, times(1)).deleteEvent(anyString());
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

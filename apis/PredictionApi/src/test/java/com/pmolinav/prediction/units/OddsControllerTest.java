package com.pmolinav.prediction.units;

import com.pmolinav.prediction.exceptions.CustomStatusException;
import com.pmolinav.prediction.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.EventType;
import com.pmolinav.predictionslib.dto.OddsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OddsControllerTest extends BaseUnitTest {

    private OddsDTO oddsDTO;
    private List<OddsDTO> expectedOdds;
    private ResponseEntity<?> result;

    /* FIND ODDS BY ID */
    @Test
    void findOddByIdHappyPath() {
        whenFindOddByIdInServiceReturnedValidOdd();
        andFindOddByIdIsCalledInController();
        thenVerifyFindOddByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseIs(oddsDTO);
    }

    @Test
    void findOddByIdNotFound() {
        whenFindOddByIdInServiceThrowsNotFoundException();
        andFindOddByIdIsCalledInController();
        thenVerifyFindOddByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findOddByIdServerError() {
        whenFindOddByIdInServiceThrowsServerException();
        andFindOddByIdIsCalledInController();
        thenVerifyFindOddByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND ODDS BY EVENT TYPE */
    @Test
    void findOddByEventTypeHappyPath() {
        whenFindOddByEventTypeInServiceReturnedValidOdd();
        andFindOddByEventTypeIsCalledInController();
        thenVerifyFindOddByEventTypeHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedOdds);
    }

    @Test
    void findOddByEventTypeNotFound() {
        whenFindOddByEventTypeInServiceThrowsNotFoundException();
        andFindOddByEventTypeIsCalledInController();
        thenVerifyFindOddByEventTypeHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findOddByEventTypeServerError() {
        whenFindOddByEventTypeInServiceThrowsServerException();
        andFindOddByEventTypeIsCalledInController();
        thenVerifyFindOddByEventTypeHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND ODDS BY EVENT TYPE */
    @Test
    void findOddByMatchIdHappyPath() {
        whenFindOddByMatchIdInServiceReturnedValidOdd();
        andFindOddByMatchIdIsCalledInController();
        thenVerifyFindOddByMatchIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedOdds);
    }

    @Test
    void findOddByMatchIdNotFound() {
        whenFindOddByMatchIdInServiceThrowsNotFoundException();
        andFindOddByMatchIdIsCalledInController();
        thenVerifyFindOddByMatchIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findOddByMatchIdServerError() {
        whenFindOddByMatchIdInServiceThrowsServerException();
        andFindOddByMatchIdIsCalledInController();
        thenVerifyFindOddByMatchIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- SETUP MOCK RETURNS ---

    private void whenFindOddByIdInServiceReturnedValidOdd() {
        oddsDTO = new OddsDTO(EventType.H2H.getName(), 1L, "LABEL1", BigDecimal.valueOf(2.0), null, true);
        when(oddsServiceMock.findById(1L)).thenReturn(oddsDTO);
    }

    private void whenFindOddByIdInServiceThrowsNotFoundException() {
        when(oddsServiceMock.findById(1L)).thenThrow(new NotFoundException("Odd not found"));
    }

    private void whenFindOddByIdInServiceThrowsServerException() {
        when(oddsServiceMock.findById(1L)).thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindOddByEventTypeInServiceReturnedValidOdd() {
        expectedOdds = List.of(
                new OddsDTO(EventType.H2H.getName(), 1L, "LABEL1", BigDecimal.valueOf(2.0), null, true)
        );
        when(oddsServiceMock.findByEventType(EventType.H2H.getName())).thenReturn(expectedOdds);
    }

    private void whenFindOddByEventTypeInServiceThrowsNotFoundException() {
        when(oddsServiceMock.findByEventType(EventType.H2H.getName())).thenThrow(new NotFoundException("Odd not found"));
    }

    private void whenFindOddByEventTypeInServiceThrowsServerException() {
        when(oddsServiceMock.findByEventType(EventType.H2H.getName())).thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindOddByMatchIdInServiceReturnedValidOdd() {
        expectedOdds = List.of(
                new OddsDTO(EventType.H2H.getName(), 1L, "LABEL1", BigDecimal.valueOf(2.0), null, true)
        );
        when(oddsServiceMock.findOddsByMatchId(1L)).thenReturn(expectedOdds);
    }

    private void whenFindOddByMatchIdInServiceThrowsNotFoundException() {
        when(oddsServiceMock.findOddsByMatchId(1L)).thenThrow(new NotFoundException("Odd not found"));
    }

    private void whenFindOddByMatchIdInServiceThrowsServerException() {
        when(oddsServiceMock.findOddsByMatchId(1L)).thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    // --- CALL CONTROLLER METHODS ---

    private void andFindOddByIdIsCalledInController() {
        result = oddsController.findOddsById(requestUid, 1L);
    }

    private void andFindOddByEventTypeIsCalledInController() {
        result = oddsController.findOddsByEventType(requestUid, EventType.H2H.getName());
    }

    private void andFindOddByMatchIdIsCalledInController() {
        result = oddsController.findOddsByMatchId(requestUid, 1L);
    }

    // --- VERIFY SERVICE CALLS ---

    private void thenVerifyFindOddByIdHasBeenCalledInService() {
        verify(oddsServiceMock, times(1)).findById(1L);
    }

    private void thenVerifyFindOddByEventTypeHasBeenCalledInService() {
        verify(oddsServiceMock, times(1)).findByEventType(EventType.H2H.getName());
    }

    private void thenVerifyFindOddByMatchIdHasBeenCalledInService() {
        verify(oddsServiceMock, times(1)).findOddsByMatchId(1L);
    }

    // --- ASSERTIONS ---

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseIs(OddsDTO expectedResponse) {
        assertNotNull(result);
        assertEquals(expectedResponse, result.getBody());
    }

    private void thenReceivedResponseListIs(List<OddsDTO> expectedResponse) {
        assertNotNull(result);
        assertEquals(expectedResponse, result.getBody());
    }
}

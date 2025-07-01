package com.pmolinav.predictions.units;

import com.pmolinav.predictions.exceptions.CustomStatusException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.EventType;
import com.pmolinav.predictionslib.dto.OddsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OddsBOControllerTest extends BaseUnitTest {

    private OddsDTO oddsDTO;
    private List<OddsDTO> expectedOdds;
    private ResponseEntity<?> result;

    /* FIND ALL ODDS */
    @Test
    void findAllOddsHappyPath() {
        whenFindAllOddsInServiceReturnedValidOdds();
        andFindAllOddsIsCalledInController();
        thenVerifyFindAllOddsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedOdds);
    }

    @Test
    void findAllOddsNotFound() {
        whenFindAllOddsInServiceThrowsNotFoundException();
        andFindAllOddsIsCalledInController();
        thenVerifyFindAllOddsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllOddsServerError() {
        whenFindAllOddsInServiceThrowsServerException();
        andFindAllOddsIsCalledInController();
        thenVerifyFindAllOddsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND ODD BY ID */
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

    /* CREATE ODD */
    @Test
    void createOddHappyPath() {
        givenValidOddsDTOForRequest();
        whenCreateOddInServiceReturnedValidId();
        andCreateOddIsCalledInController();
        thenVerifyCreateOddHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseLongIs(1L);
    }

    @Test
    void createOddBadRequest() {
        givenValidOddsDTOForRequest();
        andCreateOddIsCalledInControllerWithBindingResultErrors();
        thenReceivedStatusCodeIs(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createOddServerError() {
        givenValidOddsDTOForRequest();
        whenCreateOddInServiceThrowsServerException();
        andCreateOddIsCalledInController();
        thenVerifyCreateOddHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* UPDATE ODD */
    @Test
    void updateOddHappyPath() {
        givenValidOddsDTOForRequest();
        whenUpdateOddInServiceDoesNothing();
        andUpdateOddIsCalledInController();
        thenVerifyUpdateOddHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void updateOddNotFound() {
        givenValidOddsDTOForRequest();
        whenUpdateOddInServiceThrowsNotFoundException();
        andUpdateOddIsCalledInController();
        thenVerifyUpdateOddHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateOddServerError() {
        givenValidOddsDTOForRequest();
        whenUpdateOddInServiceThrowsServerException();
        andUpdateOddIsCalledInController();
        thenVerifyUpdateOddHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE ODD */
    @Test
    void deleteOddHappyPath() {
        whenDeleteOddInServiceDoesNothing();
        andDeleteOddIsCalledInController();
        thenVerifyDeleteOddHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteOddNotFound() {
        whenDeleteOddInServiceThrowsNotFoundException();
        andDeleteOddIsCalledInController();
        thenVerifyDeleteOddHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteOddServerError() {
        whenDeleteOddInServiceThrowsServerException();
        andDeleteOddIsCalledInController();
        thenVerifyDeleteOddHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // --- SETUP MOCK RETURNS ---

    private void givenValidOddsDTOForRequest() {
        oddsDTO = new OddsDTO(EventType.H2H.getName(), 1L, "LABEL1", BigDecimal.valueOf(2.0), "winamax_fr", true);
    }

    private void whenFindAllOddsInServiceReturnedValidOdds() {
        expectedOdds = List.of(
                new OddsDTO(EventType.H2H.getName(), 1L, "LABEL1", BigDecimal.valueOf(2.0), "winamax_fr", true),
                new OddsDTO(EventType.H2H.getName(), 1L, "LABEL2", BigDecimal.valueOf(3.5), "winamax_fr", true)
        );
        when(oddsBOServiceMock.findAll()).thenReturn(expectedOdds);
    }

    private void whenFindAllOddsInServiceThrowsNotFoundException() {
        when(oddsBOServiceMock.findAll()).thenThrow(new NotFoundException("Odds not found"));
    }

    private void whenFindAllOddsInServiceThrowsServerException() {
        when(oddsBOServiceMock.findAll()).thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindOddByIdInServiceReturnedValidOdd() {
        oddsDTO = new OddsDTO(EventType.H2H.getName(), 1L, "LABEL1", BigDecimal.valueOf(2.0), "winamax_fr", true);
        when(oddsBOServiceMock.findById(1L)).thenReturn(oddsDTO);
    }

    private void whenFindOddByIdInServiceThrowsNotFoundException() {
        when(oddsBOServiceMock.findById(1L)).thenThrow(new NotFoundException("Odd not found"));
    }

    private void whenFindOddByIdInServiceThrowsServerException() {
        when(oddsBOServiceMock.findById(1L)).thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenCreateOddInServiceReturnedValidId() {
        when(oddsBOServiceMock.create(any(OddsDTO.class))).thenReturn(1L);
    }

    private void whenCreateOddInServiceThrowsServerException() {
        when(oddsBOServiceMock.create(any(OddsDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenUpdateOddInServiceDoesNothing() {
        doNothing().when(oddsBOServiceMock).update(eq(1L), any(OddsDTO.class));
    }

    private void whenUpdateOddInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Odd not found")).when(oddsBOServiceMock).update(eq(1L), any(OddsDTO.class));
    }

    private void whenUpdateOddInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500)).when(oddsBOServiceMock).update(eq(1L), any(OddsDTO.class));
    }

    private void whenDeleteOddInServiceDoesNothing() {
        doNothing().when(oddsBOServiceMock).delete(1L);
    }

    private void whenDeleteOddInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Odd not found")).when(oddsBOServiceMock).delete(1L);
    }

    private void whenDeleteOddInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500)).when(oddsBOServiceMock).delete(1L);
    }


    // --- CALL CONTROLLER METHODS ---

    private void andFindAllOddsIsCalledInController() {
        result = oddsBOController.findAllOdds(requestUid);
    }

    private void andFindOddByIdIsCalledInController() {
        result = oddsBOController.findOddsById(requestUid, 1L);
    }

    private void andCreateOddIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = oddsBOController.createOdds(requestUid, oddsDTO, bindingResult);
    }

    private void andCreateOddIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("oddDTO", "value", "Value is mandatory")
        ));
        result = oddsBOController.createOdds(requestUid, oddsDTO, bindingResult);
    }

    private void andUpdateOddIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = oddsBOController.updateOdds(requestUid, 1L, oddsDTO, bindingResult);
    }

    private void andUpdateOddIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("oddDTO", "value", "Value is mandatory")
        ));
        result = oddsBOController.updateOdds(requestUid, 1L, oddsDTO, bindingResult);
    }

    private void andDeleteOddIsCalledInController() {
        result = oddsBOController.deleteOdds(requestUid, 1L);
    }

    // --- VERIFY SERVICE CALLS ---

    private void thenVerifyFindAllOddsHasBeenCalledInService() {
        verify(oddsBOServiceMock, times(1)).findAll();
    }

    private void thenVerifyFindOddByIdHasBeenCalledInService() {
        verify(oddsBOServiceMock, times(1)).findById(1L);
    }

    private void thenVerifyCreateOddHasBeenCalledInService() {
        verify(oddsBOServiceMock, times(1)).create(any(OddsDTO.class));
    }

    private void thenVerifyUpdateOddHasBeenCalledInService() {
        verify(oddsBOServiceMock, times(1)).update(eq(1L), any(OddsDTO.class));
    }

    private void thenVerifyDeleteOddHasBeenCalledInService() {
        verify(oddsBOServiceMock, times(1)).delete(1L);
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

    private void thenReceivedResponseLongIs(Long expectedResponse) {
        assertNotNull(result);
        assertEquals(expectedResponse, result.getBody());
    }
}

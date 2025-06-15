package com.pmolinav.predictions.units;

import com.pmolinav.predictions.exceptions.CustomStatusException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.MatchDTO;
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

class MatchBOControllerTest extends BaseUnitTest {

    private MatchDTO matchDTO;
    private List<MatchDTO> expectedMatches;
    private ResponseEntity<?> result;

    /* FIND ALL MATCHES */
    @Test
    void findAllMatchesHappyPath() {
        whenFindAllMatchesInServiceReturnedValidMatches();
        andFindAllMatchesIsCalledInController();
        thenVerifyFindAllMatchesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedMatches);
    }

    @Test
    void findAllMatchesNotFound() {
        whenFindAllMatchesInServiceThrowsNotFoundException();
        andFindAllMatchesIsCalledInController();
        thenVerifyFindAllMatchesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllMatchesServerError() {
        whenFindAllMatchesInServiceThrowsServerException();
        andFindAllMatchesIsCalledInController();
        thenVerifyFindAllMatchesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND MATCH BY ID */
    @Test
    void findMatchByIdHappyPath() {
        whenFindMatchByIdInServiceReturnedValidMatch();
        andFindMatchByIdIsCalledInController();
        thenVerifyFindMatchByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseIs(matchDTO);
    }

    @Test
    void findMatchByIdNotFound() {
        whenFindMatchByIdInServiceThrowsNotFoundException();
        andFindMatchByIdIsCalledInController();
        thenVerifyFindMatchByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findMatchByIdServerError() {
        whenFindMatchByIdInServiceThrowsServerException();
        andFindMatchByIdIsCalledInController();
        thenVerifyFindMatchByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CREATE MATCH */
    @Test
    void createMatchHappyPath() {
        givenValidMatchDTOForRequest();
        whenCreateMatchInServiceReturnedValidId();
        andCreateMatchIsCalledInController();
        thenVerifyCreateMatchHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseLongIs(1L);
    }

    @Test
    void createMatchBadRequest() {
        givenValidMatchDTOForRequest();
        andCreateMatchIsCalledInControllerWithBindingResultErrors();
        thenReceivedStatusCodeIs(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createMatchServerError() {
        givenValidMatchDTOForRequest();
        whenCreateMatchInServiceThrowsServerException();
        andCreateMatchIsCalledInController();
        thenVerifyCreateMatchHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* UPDATE MATCH */
    @Test
    void updateMatchHappyPath() {
        givenValidMatchDTOForRequest();
        whenUpdateMatchInServiceDoesNothing();
        andUpdateMatchIsCalledInController();
        thenVerifyUpdateMatchHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void updateMatchNotFound() {
        givenValidMatchDTOForRequest();
        whenUpdateMatchInServiceThrowsNotFoundException();
        andUpdateMatchIsCalledInController();
        thenVerifyUpdateMatchHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateMatchServerError() {
        givenValidMatchDTOForRequest();
        whenUpdateMatchInServiceThrowsServerException();
        andUpdateMatchIsCalledInController();
        thenVerifyUpdateMatchHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE MATCH */
    @Test
    void deleteMatchHappyPath() {
        whenDeleteMatchInServiceDoesNothing();
        andDeleteMatchIsCalledInController();
        thenVerifyDeleteMatchHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteMatchNotFound() {
        whenDeleteMatchInServiceThrowsNotFoundException();
        andDeleteMatchIsCalledInController();
        thenVerifyDeleteMatchHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteMatchServerError() {
        whenDeleteMatchInServiceThrowsServerException();
        andDeleteMatchIsCalledInController();
        thenVerifyDeleteMatchHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // --- SETUP MOCK RETURNS ---

    private void givenValidMatchDTOForRequest() {
        matchDTO = new MatchDTO(1L, "PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, "ACTIVE");
    }

    private void whenFindAllMatchesInServiceReturnedValidMatches() {
        expectedMatches = List.of(
                new MatchDTO(1L, "PREMIER", 2025, 3,
                        "Team A", "Team B", 1234567L, "ACTIVE"),
                new MatchDTO(2L, "PREMIER", 2025, 3,
                        "Team A", "Team B", 1234567L, "ACTIVE")
        );
        when(matchBOServiceMock.findAllMatches()).thenReturn(expectedMatches);
    }

    private void whenFindAllMatchesInServiceThrowsNotFoundException() {
        when(matchBOServiceMock.findAllMatches()).thenThrow(new NotFoundException("Matches not found"));
    }

    private void whenFindAllMatchesInServiceThrowsServerException() {
        when(matchBOServiceMock.findAllMatches()).thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindMatchByIdInServiceReturnedValidMatch() {
        matchDTO = new MatchDTO(1L, "PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, "ACTIVE");
        when(matchBOServiceMock.findMatchById(1L)).thenReturn(matchDTO);
    }

    private void whenFindMatchByIdInServiceThrowsNotFoundException() {
        when(matchBOServiceMock.findMatchById(1L)).thenThrow(new NotFoundException("Match not found"));
    }

    private void whenFindMatchByIdInServiceThrowsServerException() {
        when(matchBOServiceMock.findMatchById(1L)).thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenCreateMatchInServiceReturnedValidId() {
        when(matchBOServiceMock.createMatch(any(MatchDTO.class))).thenReturn(1L);
    }

    private void whenCreateMatchInServiceThrowsServerException() {
        when(matchBOServiceMock.createMatch(any(MatchDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenUpdateMatchInServiceDoesNothing() {
        doNothing().when(matchBOServiceMock).updateMatch(eq(1L), any(MatchDTO.class));
    }

    private void whenUpdateMatchInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Match not found")).when(matchBOServiceMock).updateMatch(eq(1L), any(MatchDTO.class));
    }

    private void whenUpdateMatchInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500)).when(matchBOServiceMock).updateMatch(eq(1L), any(MatchDTO.class));
    }

    private void whenDeleteMatchInServiceDoesNothing() {
        doNothing().when(matchBOServiceMock).deleteMatch(1L);
    }

    private void whenDeleteMatchInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Match not found")).when(matchBOServiceMock).deleteMatch(1L);
    }

    private void whenDeleteMatchInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500)).when(matchBOServiceMock).deleteMatch(1L);
    }


    // --- CALL CONTROLLER METHODS ---

    private void andFindAllMatchesIsCalledInController() {
        result = matchBOController.findAllMatches(requestUid);
    }

    private void andFindMatchByIdIsCalledInController() {
        result = matchBOController.findMatchById(requestUid, 1L);
    }

    private void andCreateMatchIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = matchBOController.createMatch(requestUid, matchDTO, bindingResult);
    }

    private void andCreateMatchIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("matchDTO", "name", "Name is mandatory")
        ));
        result = matchBOController.createMatch(requestUid, matchDTO, bindingResult);
    }

    private void andUpdateMatchIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = matchBOController.updateMatch(requestUid, 1L, matchDTO, bindingResult);
    }

    private void andUpdateMatchIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("matchDTO", "name", "Name is mandatory")
        ));
        result = matchBOController.updateMatch(requestUid, 1L, matchDTO, bindingResult);
    }

    private void andDeleteMatchIsCalledInController() {
        result = matchBOController.deleteMatch(requestUid, 1L);
    }

    // --- VERIFY SERVICE METHOD CALLS ---

    private void thenVerifyFindAllMatchesHasBeenCalledInService() {
        verify(matchBOServiceMock, times(1)).findAllMatches();
    }

    private void thenVerifyFindMatchByIdHasBeenCalledInService() {
        verify(matchBOServiceMock, times(1)).findMatchById(1L);
    }

    private void thenVerifyCreateMatchHasBeenCalledInService() {
        verify(matchBOServiceMock, times(1)).createMatch(any(MatchDTO.class));
    }

    private void thenVerifyUpdateMatchHasBeenCalledInService() {
        verify(matchBOServiceMock, times(1)).updateMatch(eq(1L), any(MatchDTO.class));
    }

    private void thenVerifyDeleteMatchHasBeenCalledInService() {
        verify(matchBOServiceMock, times(1)).deleteMatch(1L);
    }

    // --- ASSERTIONS ---

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseIs(MatchDTO expectedResponse) {
        assertNotNull(result);
        assertEquals(expectedResponse, result.getBody());
    }

    private void thenReceivedResponseListIs(List<MatchDTO> expectedResponse) {
        assertNotNull(result);
        assertEquals(expectedResponse, result.getBody());
    }

    private void thenReceivedResponseLongIs(Long expectedResponse) {
        assertNotNull(result);
        assertEquals(expectedResponse, result.getBody());
    }
}

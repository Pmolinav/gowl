package com.pmolinav.prediction.units;

import com.pmolinav.prediction.exceptions.CustomStatusException;
import com.pmolinav.prediction.exceptions.NotFoundException;
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

class MatchControllerTest extends BaseUnitTest {

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
        when(matchServiceMock.findAllMatches()).thenReturn(expectedMatches);
    }

    private void whenFindAllMatchesInServiceThrowsNotFoundException() {
        when(matchServiceMock.findAllMatches()).thenThrow(new NotFoundException("Matches not found"));
    }

    private void whenFindAllMatchesInServiceThrowsServerException() {
        when(matchServiceMock.findAllMatches()).thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindMatchByIdInServiceReturnedValidMatch() {
        matchDTO = new MatchDTO(1L, "PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, "ACTIVE");
        when(matchServiceMock.findMatchById(1L)).thenReturn(matchDTO);
    }

    private void whenFindMatchByIdInServiceThrowsNotFoundException() {
        when(matchServiceMock.findMatchById(1L)).thenThrow(new NotFoundException("Match not found"));
    }

    private void whenFindMatchByIdInServiceThrowsServerException() {
        when(matchServiceMock.findMatchById(1L)).thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenCreateMatchInServiceReturnedValidId() {
        when(matchServiceMock.createMatch(any(MatchDTO.class))).thenReturn(1L);
    }

    private void whenCreateMatchInServiceThrowsServerException() {
        when(matchServiceMock.createMatch(any(MatchDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenUpdateMatchInServiceDoesNothing() {
        doNothing().when(matchServiceMock).updateMatch(eq(1L), any(MatchDTO.class));
    }

    private void whenUpdateMatchInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Match not found")).when(matchServiceMock).updateMatch(eq(1L), any(MatchDTO.class));
    }

    private void whenUpdateMatchInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500)).when(matchServiceMock).updateMatch(eq(1L), any(MatchDTO.class));
    }

    private void whenDeleteMatchInServiceDoesNothing() {
        doNothing().when(matchServiceMock).deleteMatch(1L);
    }

    private void whenDeleteMatchInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Match not found")).when(matchServiceMock).deleteMatch(1L);
    }

    private void whenDeleteMatchInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500)).when(matchServiceMock).deleteMatch(1L);
    }


    // --- CALL CONTROLLER METHODS ---

    private void andFindAllMatchesIsCalledInController() {
        result = matchController.findAllMatches(requestUid);
    }

    private void andFindMatchByIdIsCalledInController() {
        result = matchController.findMatchById(requestUid, 1L);
    }

    private void andCreateMatchIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = matchController.createMatch(requestUid, matchDTO, bindingResult);
    }

    private void andCreateMatchIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("matchDTO", "name", "Name is mandatory")
        ));
        result = matchController.createMatch(requestUid, matchDTO, bindingResult);
    }

    private void andUpdateMatchIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = matchController.updateMatch(requestUid, 1L, matchDTO, bindingResult);
    }

    private void andUpdateMatchIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("matchDTO", "name", "Name is mandatory")
        ));
        result = matchController.updateMatch(requestUid, 1L, matchDTO, bindingResult);
    }

    private void andDeleteMatchIsCalledInController() {
        result = matchController.deleteMatch(requestUid, 1L);
    }

    // --- VERIFY SERVICE METHOD CALLS ---

    private void thenVerifyFindAllMatchesHasBeenCalledInService() {
        verify(matchServiceMock, times(1)).findAllMatches();
    }

    private void thenVerifyFindMatchByIdHasBeenCalledInService() {
        verify(matchServiceMock, times(1)).findMatchById(1L);
    }

    private void thenVerifyCreateMatchHasBeenCalledInService() {
        verify(matchServiceMock, times(1)).createMatch(any(MatchDTO.class));
    }

    private void thenVerifyUpdateMatchHasBeenCalledInService() {
        verify(matchServiceMock, times(1)).updateMatch(eq(1L), any(MatchDTO.class));
    }

    private void thenVerifyDeleteMatchHasBeenCalledInService() {
        verify(matchServiceMock, times(1)).deleteMatch(1L);
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

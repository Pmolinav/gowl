package com.pmolinav.prediction.units;

import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.dto.MatchStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MatchControllerTest extends BaseUnitTest {

    private MatchDTO matchDTO;
    private List<MatchDTO> expectedMatches;
    private ResponseEntity<?> result;

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

    /* FIND MATCHES BY CATEGORY ID, SEASON AND MATCH DAY NUMBER */
    @Test
    void findByCategoryIdSeasonAndMatchDayNumberHappyPath() {
        whenFindMatchByCategoryIdAndSeasonAndMatchDayNumberInServiceReturnedValidMatch();
        andFindMatchByCategoryIdAndSeasonAndMatchDayNumberIsCalledInController();
        thenVerifyFindMatchByCategoryIdAndSeasonAndMatchDayNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedMatches);
    }

    @Test
    void findByCategoryIdSeasonAndMatchDayNumberNotFound() {
        whenFindMatchByCategoryIdAndSeasonAndMatchDayNumberInServiceThrowsNotFoundException();
        andFindMatchByCategoryIdAndSeasonAndMatchDayNumberIsCalledInController();
        thenVerifyFindMatchByCategoryIdAndSeasonAndMatchDayNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findByCategoryIdSeasonAndMatchDayNumberServerError() {
        whenFindMatchByCategoryIdAndSeasonAndMatchDayNumberInServiceThrowsServerException();
        andFindMatchByCategoryIdAndSeasonAndMatchDayNumberIsCalledInController();
        thenVerifyFindMatchByCategoryIdAndSeasonAndMatchDayNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- SETUP MOCK RETURNS ---

    private void whenFindMatchByIdInServiceReturnedValidMatch() {
        matchDTO = new MatchDTO("PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, MatchStatus.ACTIVE);
        when(matchServiceMock.findMatchById(1L)).thenReturn(matchDTO);
    }

    private void whenFindMatchByIdInServiceThrowsNotFoundException() {
        when(matchServiceMock.findMatchById(1L)).thenThrow(new NotFoundException("Match not found"));
    }

    private void whenFindMatchByIdInServiceThrowsServerException() {
        when(matchServiceMock.findMatchById(1L)).thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindMatchByCategoryIdAndSeasonAndMatchDayNumberInServiceReturnedValidMatch() {
        expectedMatches = List.of(new MatchDTO("PREMIER", 2025, 3,
                "Team A", "Team B", 1234567L, MatchStatus.ACTIVE)
        );
        when(matchServiceMock.findByCategoryIdAndSeasonAndMatchDayNumber("PREMIER", 2025, 3)).thenReturn(expectedMatches);
    }

    private void whenFindMatchByCategoryIdAndSeasonAndMatchDayNumberInServiceThrowsNotFoundException() {
        when(matchServiceMock.findByCategoryIdAndSeasonAndMatchDayNumber("PREMIER", 2025, 3))
                .thenThrow(new NotFoundException("Match not found"));
    }

    private void whenFindMatchByCategoryIdAndSeasonAndMatchDayNumberInServiceThrowsServerException() {
        when(matchServiceMock.findByCategoryIdAndSeasonAndMatchDayNumber("PREMIER", 2025, 3))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    // --- CALL CONTROLLER METHODS ---

    private void andFindMatchByIdIsCalledInController() {
        result = matchController.findMatchById(requestUid, 1L);
    }

    private void andFindMatchByCategoryIdAndSeasonAndMatchDayNumberIsCalledInController() {
        result = matchController.findByCategoryIdSeasonAndMatchDayNumber(requestUid, "PREMIER", 2025, 3);
    }

    // --- VERIFY SERVICE METHOD CALLS ---

    private void thenVerifyFindMatchByIdHasBeenCalledInService() {
        verify(matchServiceMock, times(1)).findMatchById(1L);
    }

    private void thenVerifyFindMatchByCategoryIdAndSeasonAndMatchDayNumberHasBeenCalledInService() {
        verify(matchServiceMock, times(1)).findByCategoryIdAndSeasonAndMatchDayNumber("PREMIER", 2025, 3);
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

}

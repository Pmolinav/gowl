package com.pmolinav.league.units;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MatchDayControllerTest extends BaseUnitTest {

    List<MatchDayDTO> expectedMatchDay;
    ResponseEntity<?> result;

    /* FIND MATCH DAYS BY CATEGORY ID AND SEASON */
    @Test
    void findMatchDaysByCategoryIdAndSeasonHappyPath() {
        whenFindMatchDayByCategoryIdAndSeasonInServiceReturnedValidMatchDays();
        andFindMatchDaysByCategoryIdAndSeasonIsCalledInController();
        thenVerifyFindMatchDaysByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsMatchDayListIs(expectedMatchDay);
    }

    @Test
    void findMatchDaysCategoryByIdAndSeasonNotFound() {
        whenFindMatchDaysByCategoryIdAndSeasonInServiceThrowsNotFoundException();
        andFindMatchDaysByCategoryIdAndSeasonIsCalledInController();
        thenVerifyFindMatchDaysByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findMatchDaysByCategoryIdAndSeasonServerError() {
        whenFindMatchDaysByCategoryIdAndSeasonInServiceThrowsServerException();
        andFindMatchDaysByCategoryIdAndSeasonIsCalledInController();
        thenVerifyFindMatchDaysByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void whenFindMatchDayByCategoryIdAndSeasonInServiceReturnedValidMatchDays() {
        expectedMatchDay = List.of(new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L));

        when(matchDaysServiceMock.findMatchDayByCategoryIdAndSeason("PREMIER", 2025)).thenReturn(expectedMatchDay);
    }

    private void whenFindMatchDaysByCategoryIdAndSeasonInServiceThrowsNotFoundException() {
        when(matchDaysServiceMock.findMatchDayByCategoryIdAndSeason("PREMIER", 2025)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindMatchDaysByCategoryIdAndSeasonInServiceThrowsServerException() {
        when(matchDaysServiceMock.findMatchDayByCategoryIdAndSeason("PREMIER", 2025))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void andFindMatchDaysByCategoryIdAndSeasonIsCalledInController() {
        result = matchDayController.findMatchDaysByCategoryIdAndSeason(this.requestUid, "PREMIER", 2025);
    }

    private void thenVerifyFindMatchDaysByCategoryIdAndSeasonHasBeenCalledInService() {
        verify(matchDaysServiceMock, times(1)).findMatchDayByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsMatchDayListIs(List<MatchDayDTO> expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }
}

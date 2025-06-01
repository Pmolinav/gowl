package com.pmolinav.league.units;

import com.pmolinav.league.exceptions.InternalServerErrorException;
import com.pmolinav.league.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LeaguePlayerPointsControllerTest extends BaseUnitTest {

    LeaguePlayerPointsDTO leaguePlayerPointsDTO;
    List<LeaguePlayerPointsDTO> expectedLeaguePlayerPoints;
    ResponseEntity<?> result;

    /* FIND LEAGUE PLAYER POINTS BY LEAGUE ID AND USERNAME */
    @Test
    void findLeaguePlayerPointsByLeagueIdAndUsernameHappyPath() {
        whenFindLeaguePlayerPointsByLeagueIdAndUsernameInServiceReturnedValidData();
        andFindLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController();
        thenVerifyFindLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsLeaguePlayerListIs(expectedLeaguePlayerPoints);
    }

    @Test
    void findLeaguePlayerPointsByLeagueIdAndUsernameNotFound() {
        whenFindLeaguePlayerPointsByLeagueIdAndUsernameInServiceThrowsNotFoundException();
        andFindLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController();
        thenVerifyFindLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeaguePlayerPointsByLeagueIdAndUsernameServerError() {
        whenFindLeaguePlayerPointsByLeagueIdInServiceThrowsServerException();
        andFindLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController();
        thenVerifyFindLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND LEAGUE PLAYER POINTS BY CATEGORY ID, SEASON AND NUMBER */
    @Test
    void findLeaguePlayerPointsByCategoryIdSeasonAndNumberHappyPath() {
        whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceReturnedValidData();
        andFindLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyFindLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsLeaguePlayerListIs(expectedLeaguePlayerPoints);
    }

    @Test
    void findLeaguePlayerPointsByCategoryIdSeasonAndNumberNotFound() {
        whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException();
        andFindLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyFindLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeaguePlayerPointsByCategoryIdSeasonAndNumberServerError() {
        whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceThrowsServerException();
        andFindLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyFindLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void whenFindLeaguePlayerPointsByLeagueIdAndUsernameInServiceReturnedValidData() {
        expectedLeaguePlayerPoints = List.of(
                new LeaguePlayerPointsDTO("PREMIER",
                        2025, 4, 1L, "someUser", 26),
                new LeaguePlayerPointsDTO("PREMIER",
                        2025, 5, 1L, "someUser", 26)
        );

        when(leaguePlayerPointsServiceMock.findLeaguePlayerPointsByLeagueIdAndPlayer(1L, "someUser"))
                .thenReturn(expectedLeaguePlayerPoints);
    }

    private void whenFindLeaguePlayerPointsByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        when(leaguePlayerPointsServiceMock.findLeaguePlayerPointsByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayerPointsByLeagueIdInServiceThrowsServerException() {
        when(leaguePlayerPointsServiceMock.findLeaguePlayerPointsByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceReturnedValidData() {
        expectedLeaguePlayerPoints = List.of(
                new LeaguePlayerPointsDTO("PREMIER",
                        2025, 4, 1L, "someUser", 26),
                new LeaguePlayerPointsDTO("PREMIER",
                        2025, 4, 1L, "otherUser", 234)
        );

        when(leaguePlayerPointsServiceMock.findLeaguePlayerPointsByCategorySeasonAndNumber("PREMIER", 2025, 4))
                .thenReturn(expectedLeaguePlayerPoints);
    }

    private void whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException() {
        when(leaguePlayerPointsServiceMock.findLeaguePlayerPointsByCategorySeasonAndNumber("PREMIER", 2025, 4))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceThrowsServerException() {
        when(leaguePlayerPointsServiceMock.findLeaguePlayerPointsByCategorySeasonAndNumber("PREMIER", 2025, 4))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void andFindLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerPointsController.findLeaguePlayerPointsByLeagueIdAndPlayer(this.requestUid, 1L, "someUser");
    }

    private void andFindLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController() {
        result = leaguePlayerPointsController.findLeaguePlayerPointsByCategorySeasonAndNumber(this.requestUid, "PREMIER", 2025, 4);
    }

    private void thenVerifyFindLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayerPointsServiceMock, times(1))
                .findLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void thenVerifyFindLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService() {
        verify(leaguePlayerPointsServiceMock, times(1))
                .findLeaguePlayerPointsByCategorySeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsLeaguePlayerListIs(List<LeaguePlayerPointsDTO> expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

}

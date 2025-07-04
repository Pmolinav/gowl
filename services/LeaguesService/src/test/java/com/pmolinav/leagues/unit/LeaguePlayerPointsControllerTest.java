package com.pmolinav.leagues.unit;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
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

    /* CREATE LEAGUE PLAYER POINTS */
    @Test
    void createLeaguePlayerPointsHappyPath() {
        givenValidLeaguePlayersDTOForRequest("someUser");
        whenCreateLeaguePlayerPointsInServiceReturnedValidData();
        andCreateLeaguePlayerPointsIsCalledInController();
        thenVerifyCreateLeaguePlayerPointsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsLeaguePlayerPointsIs(expectedLeaguePlayerPoints.getFirst());
    }

    @Test
    void createLeaguePlayerServerError() {
        givenValidLeaguePlayersDTOForRequest("otherUser");
        whenCreateLeaguePlayerInServiceThrowsServerException();
        andCreateLeaguePlayerPointsIsCalledInController();
        thenVerifyCreateLeaguePlayerPointsHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE LEAGUE PLAYER POINTS BY LEAGUE ID AND USERNAME*/
    @Test
    void deleteLeaguePlayerPointsByLeagueIdAndUsernameHappyPath() {
        whenDeleteLeaguePlayerPointsByLeagueIdAndUsernameInServiceIsOk();
        andDeleteLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController();
        thenVerifyDeleteLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteLeaguePlayerPointsByLeagueIdAndUsernameNotFound() {
        whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsNotFoundException();
        andDeleteLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController();
        thenVerifyDeleteLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteLeaguePlayerPointsByLeagueIdAndUsernameServerError() {
        whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsServerException();
        andDeleteLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController();
        thenVerifyDeleteLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE LEAGUE PLAYER POINTS BY CATEGORY ID, SEASON AND NUMBER*/
    @Test
    void deleteLeaguePlayerPointsByCategoryIdSeasonAndNumberHappyPath() {
        whenDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceIsOk();
        andDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteLeaguePlayerPointsByCategoryIdSeasonAndNumberNotFound() {
        whenDeleteLeaguePlayerByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException();
        andDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteLeaguePlayerPointsByCategoryIdSeasonAndNumberServerError() {
        whenDeleteLeaguePlayerByCategoryIdSeasonAndNumberInServiceThrowsServerException();
        andDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void givenValidLeaguePlayersDTOForRequest(String username) {
        leaguePlayerPointsDTO = new LeaguePlayerPointsDTO("PREMIER",
                2025, 4, 1L, username, 26);
    }

    private void whenFindLeaguePlayerPointsByLeagueIdAndUsernameInServiceReturnedValidData() {
        expectedLeaguePlayerPoints = List.of(
                new LeaguePlayerPointsDTO("PREMIER",
                        2025, 4, 1L, "someUser", 26),
                new LeaguePlayerPointsDTO("PREMIER",
                        2025, 5, 1L, "someUser", 26)
        );

        when(leaguePlayerPointsServiceMock.findByLeagueIdAndPlayer(1L, "someUser"))
                .thenReturn(expectedLeaguePlayerPoints);
    }

    private void whenFindLeaguePlayerPointsByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        when(leaguePlayerPointsServiceMock.findByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayerPointsByLeagueIdInServiceThrowsServerException() {
        when(leaguePlayerPointsServiceMock.findByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceReturnedValidData() {
        expectedLeaguePlayerPoints = List.of(
                new LeaguePlayerPointsDTO("PREMIER",
                        2025, 4, 1L, "someUser", 26),
                new LeaguePlayerPointsDTO("PREMIER",
                        2025, 4, 1L, "otherUser", 234)
        );

        when(leaguePlayerPointsServiceMock.findByCategoryIdSeasonAndNumber("PREMIER", 2025, 4))
                .thenReturn(expectedLeaguePlayerPoints);
    }

    private void whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException() {
        when(leaguePlayerPointsServiceMock.findByCategoryIdSeasonAndNumber("PREMIER", 2025, 4))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceThrowsServerException() {
        when(leaguePlayerPointsServiceMock.findByCategoryIdSeasonAndNumber("PREMIER", 2025, 4))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateLeaguePlayerPointsInServiceReturnedValidData() {
        expectedLeaguePlayerPoints = List.of(new LeaguePlayerPointsDTO("PREMIER",
                2025, 4, 1L, "someUser", 26));

        when(leaguePlayerPointsServiceMock.createOrUpdateLeaguePlayerPoints(any()))
                .thenReturn(expectedLeaguePlayerPoints.getFirst());
    }

    private void whenCreateLeaguePlayerInServiceThrowsServerException() {
        when(leaguePlayerPointsServiceMock.createOrUpdateLeaguePlayerPoints(any()))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenDeleteLeaguePlayerPointsByLeagueIdAndUsernameInServiceIsOk() {
        doNothing().when(leaguePlayerPointsServiceMock).deleteLeaguePlayerPointsByLeagueIdAndUsername(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayerPointsServiceMock)
                .deleteLeaguePlayerPointsByLeagueIdAndUsername(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayerPointsServiceMock)
                .deleteLeaguePlayerPointsByLeagueIdAndUsername(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceIsOk() {
        doNothing().when(leaguePlayerPointsServiceMock)
                .deleteLeaguePlayerPointsByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void whenDeleteLeaguePlayerByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayerPointsServiceMock)
                .deleteLeaguePlayerPointsByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void whenDeleteLeaguePlayerByCategoryIdSeasonAndNumberInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayerPointsServiceMock)
                .deleteLeaguePlayerPointsByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void andFindLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerPointsController.findLeaguePlayerPointsByLeagueIdAndPlayer(1L, "someUser");
    }

    private void andFindLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController() {
        result = leaguePlayerPointsController.findLeaguePlayerPointsByCategorySeasonAndNumber("PREMIER", 2025, 4);
    }

    private void andCreateLeaguePlayerPointsIsCalledInController() {
        result = leaguePlayerPointsController.createOrUpdateLeaguePlayersPoints(leaguePlayerPointsDTO);
    }

    private void andDeleteLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerPointsController.deleteLeaguePlayerPointsByLeagueIdAndPlayer(1L, "someUser");
    }

    private void andDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController() {
        result = leaguePlayerPointsController.deleteLeaguePlayerByCategorySeasonAndNumber("PREMIER", 2025, 4);
    }

    private void thenVerifyFindLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayerPointsServiceMock, times(1))
                .findByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void thenVerifyFindLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService() {
        verify(leaguePlayerPointsServiceMock, times(1))
                .findByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void thenVerifyCreateLeaguePlayerPointsHasBeenCalledInService() {
        verify(leaguePlayerPointsServiceMock, times(1))
                .createOrUpdateLeaguePlayerPoints(any());
    }

    private void thenVerifyDeleteLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayerPointsServiceMock, times(1))
                .deleteLeaguePlayerPointsByLeagueIdAndUsername(anyLong(), anyString());
    }

    private void thenVerifyDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService() {
        verify(leaguePlayerPointsServiceMock, times(1))
                .deleteLeaguePlayerPointsByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsLeaguePlayerListIs(List<LeaguePlayerPointsDTO> expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

    private void thenReceivedResponseBodyAsLeaguePlayerPointsIs(LeaguePlayerPointsDTO expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }
}

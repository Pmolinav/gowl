package com.pmolinav.leagues.units;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeaguePlayerPointsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LeaguePlayerPointsBOControllerTest extends BaseUnitTest {

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

        when(leaguePlayerPointsBOServiceMock.findLeaguePlayerPointsByLeagueIdAndPlayer(1L, "someUser"))
                .thenReturn(expectedLeaguePlayerPoints);
    }

    private void whenFindLeaguePlayerPointsByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        when(leaguePlayerPointsBOServiceMock.findLeaguePlayerPointsByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayerPointsByLeagueIdInServiceThrowsServerException() {
        when(leaguePlayerPointsBOServiceMock.findLeaguePlayerPointsByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceReturnedValidData() {
        expectedLeaguePlayerPoints = List.of(
                new LeaguePlayerPointsDTO("PREMIER",
                        2025, 4, 1L, "someUser", 26),
                new LeaguePlayerPointsDTO("PREMIER",
                        2025, 4, 1L, "otherUser", 234)
        );

        when(leaguePlayerPointsBOServiceMock.findLeaguePlayerPointsByCategorySeasonAndNumber("PREMIER", 2025, 4))
                .thenReturn(expectedLeaguePlayerPoints);
    }

    private void whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException() {
        when(leaguePlayerPointsBOServiceMock.findLeaguePlayerPointsByCategorySeasonAndNumber("PREMIER", 2025, 4))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceThrowsServerException() {
        when(leaguePlayerPointsBOServiceMock.findLeaguePlayerPointsByCategorySeasonAndNumber("PREMIER", 2025, 4))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateLeaguePlayerPointsInServiceReturnedValidData() {
        expectedLeaguePlayerPoints = List.of(new LeaguePlayerPointsDTO("PREMIER",
                2025, 4, 1L, "someUser", 26));

        when(leaguePlayerPointsBOServiceMock.createOrUpdateLeaguePlayerPoints(any()))
                .thenReturn(expectedLeaguePlayerPoints.getFirst());
    }

    private void whenCreateLeaguePlayerInServiceThrowsServerException() {
        when(leaguePlayerPointsBOServiceMock.createOrUpdateLeaguePlayerPoints(any()))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenDeleteLeaguePlayerPointsByLeagueIdAndUsernameInServiceIsOk() {
        doNothing().when(leaguePlayerPointsBOServiceMock).deleteLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayerPointsBOServiceMock)
                .deleteLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayerPointsBOServiceMock)
                .deleteLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberInServiceIsOk() {
        doNothing().when(leaguePlayerPointsBOServiceMock)
                .deleteLeaguePlayerByCategorySeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void whenDeleteLeaguePlayerByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayerPointsBOServiceMock)
                .deleteLeaguePlayerByCategorySeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void whenDeleteLeaguePlayerByCategoryIdSeasonAndNumberInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayerPointsBOServiceMock)
                .deleteLeaguePlayerByCategorySeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void andFindLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerPointsBOController.findLeaguePlayerPointsByLeagueIdAndPlayer(this.requestUid, 1L, "someUser");
    }

    private void andFindLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController() {
        result = leaguePlayerPointsBOController.findLeaguePlayerPointsByCategorySeasonAndNumber(this.requestUid, "PREMIER", 2025, 4);
    }

    private void andCreateLeaguePlayerPointsIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = leaguePlayerPointsBOController.createOrUpdateLeaguePlayersPoints(this.requestUid, leaguePlayerPointsDTO, bindingResult);
    }

    private void andDeleteLeaguePlayerPointsByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerPointsBOController.deleteLeaguePlayerPointsByLeagueIdAndPlayer(this.requestUid, 1L, "someUser");
    }

    private void andDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberIsCalledInController() {
        result = leaguePlayerPointsBOController.deleteLeaguePlayerByCategorySeasonAndNumber(this.requestUid, "PREMIER", 2025, 4);
    }

    private void thenVerifyFindLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayerPointsBOServiceMock, times(1))
                .findLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void thenVerifyFindLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService() {
        verify(leaguePlayerPointsBOServiceMock, times(1))
                .findLeaguePlayerPointsByCategorySeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void thenVerifyCreateLeaguePlayerPointsHasBeenCalledInService() {
        verify(leaguePlayerPointsBOServiceMock, times(1))
                .createOrUpdateLeaguePlayerPoints(any());
    }

    private void thenVerifyDeleteLeaguePlayerPointsByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayerPointsBOServiceMock, times(1))
                .deleteLeaguePlayerPointsByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void thenVerifyDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumberHasBeenCalledInService() {
        verify(leaguePlayerPointsBOServiceMock, times(1))
                .deleteLeaguePlayerByCategorySeasonAndNumber(anyString(), anyInt(), anyInt());
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

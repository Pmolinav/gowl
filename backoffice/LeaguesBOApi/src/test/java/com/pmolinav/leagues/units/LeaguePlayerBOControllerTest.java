package com.pmolinav.leagues.units;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import com.pmolinav.leagueslib.model.LeagueStatus;
import com.pmolinav.leagueslib.model.PlayerStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LeaguePlayerBOControllerTest extends BaseUnitTest {

    List<LeaguePlayerDTO> leaguePlayersDTO;
    List<LeaguePlayerDTO> expectedLeaguePlayers;
    List<LeaguePlayerId> expectedLeaguePlayerIds;
    List<LeagueDTO> expectedLeagues;
    ResponseEntity<?> result;

    /* FIND LEAGUE PLAYERS BY LEAGUE ID */
    @Test
    void findLeaguePlayersByLeagueIdHappyPath() {
        whenFindLeaguePlayersByLeagueIdInServiceReturnedValidLeaguePlayers();
        andFindLeaguePlayersByLeagueIdIsCalledInController();
        thenVerifyFindLeaguePlayersByLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsLeaguePlayerListIs(expectedLeaguePlayers);
    }

    @Test
    void findLeaguePlayersByLeagueIdNotFound() {
        whenFindLeaguePlayersByLeagueIdInServiceThrowsNotFoundException();
        andFindLeaguePlayersByLeagueIdIsCalledInController();
        thenVerifyFindLeaguePlayersByLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeaguePlayersByLeagueIdServerError() {
        whenFindLeaguePlayersByLeagueIdInServiceThrowsServerException();
        andFindLeaguePlayersByLeagueIdIsCalledInController();
        thenVerifyFindLeaguePlayersByLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND LEAGUE PLAYERS BY LEAGUE ID AND USERNAME */
    @Test
    void findLeaguePlayersByLeagueIdAndUsernameHappyPath() {
        whenFindLeaguePlayersByLeagueIdAndUsernameInServiceReturnedValidLeaguePlayers();
        andFindLeaguePlayersByLeagueIdAndUsernameIsCalledInController();
        thenVerifyFindLeaguePlayersByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsLeaguePlayerIs(expectedLeaguePlayers.getFirst());
    }

    @Test
    void findLeaguePlayersByLeagueIdAndUsernameAndUsernameNotFound() {
        whenFindLeaguePlayersByLeagueIdAndUsernameInServiceThrowsNotFoundException();
        andFindLeaguePlayersByLeagueIdAndUsernameIsCalledInController();
        thenVerifyFindLeaguePlayersByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeaguePlayersByLeagueIdAndUsernameAndUsernameServerError() {
        whenFindLeaguePlayersByLeagueIdAndUsernameInServiceThrowsServerException();
        andFindLeaguePlayersByLeagueIdAndUsernameIsCalledInController();
        thenVerifyFindLeaguePlayersByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND LEAGUES BY USERNAME */
    @Test
    void findLeaguesByUsernameHappyPath() {
        whenFindLeaguesByUsernameInServiceReturnedValidLeaguePlayers();
        andFindLeaguesByUsernameIsCalledInController();
        thenVerifyFindLeaguesByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsLeagueListIs(expectedLeagues);
    }

    @Test
    void findLeaguesByUsernameNotFound() {
        whenFindLeaguesByUsernameInServiceThrowsNotFoundException();
        andFindLeaguesByUsernameIsCalledInController();
        thenVerifyFindLeaguesByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeaguesByUsernameServerError() {
        whenFindLeaguesByUsernameInServiceThrowsServerException();
        andFindLeaguesByUsernameIsCalledInController();
        thenVerifyFindLeaguesByUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CREATE LEAGUE PLAYERS */
    @Test
    void createLeaguePlayersHappyPath() {
        givenValidLeaguePlayersDTOForRequest("someUser", "otherUser");
        whenCreateLeaguePlayerInServiceReturnedAValidLeaguePlayer();
        andCreateLeaguePlayerIsCalledInController();
        thenVerifyCreateLeaguePlayerHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsLeaguePlayerIdIs(expectedLeaguePlayerIds);
    }

    @Test
    void createLeaguePlayerServerError() {
        givenValidLeaguePlayersDTOForRequest("otherUser", "someUser");
        whenCreateLeaguePlayerInServiceThrowsServerException();
        andCreateLeaguePlayerIsCalledInController();
        thenVerifyCreateLeaguePlayerHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* ADD POINTS TO LEAGUE PLAYER*/
    @Test
    void addPointsToLeaguePlayerHappyPath() {
        whenAddPointsToLeaguePlayerInServiceIsOk();
        andAddPointsToLeaguePlayerIsCalledInController();
        thenVerifyAddPointsToLeaguePlayerHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void addPointsToLeaguePlayerNotFound() {
        whenAddPointsToLeaguePlayerInServiceThrowsNotFoundException();
        andAddPointsToLeaguePlayerIsCalledInController();
        thenVerifyAddPointsToLeaguePlayerHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void addPointsToLeaguePlayerServerError() {
        whenAddPointsToLeaguePlayerInServiceThrowsServerException();
        andAddPointsToLeaguePlayerIsCalledInController();
        thenVerifyAddPointsToLeaguePlayerHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE LEAGUE PLAYERS BY LEAGUE ID*/
    @Test
    void deleteLeaguePlayersLeagueIdHappyPath() {
        whenDeleteLeaguePlayerLeagueIdInServiceIsOk();
        andDeleteLeaguePlayerLeagueIdIsCalledInController();
        thenVerifyDeleteLeaguePlayerLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteLeaguePlayersLeagueIdNotFound() {
        whenDeleteLeaguePlayerLeagueIdInServiceThrowsNotFoundException();
        andDeleteLeaguePlayerLeagueIdIsCalledInController();
        thenVerifyDeleteLeaguePlayerLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteLeaguePlayersLeagueIdServerError() {
        whenDeleteLeaguePlayerLeagueIdInServiceThrowsServerException();
        andDeleteLeaguePlayerLeagueIdIsCalledInController();
        thenVerifyDeleteLeaguePlayerLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE LEAGUE PLAYER BY LEAGUE ID AND USERNAME*/
    @Test
    void deleteLeaguePlayersByLeagueIdAndPlayerHappyPath() {
        whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceIsOk();
        andDeleteLeaguePlayerByLeagueIdAndUsernameIsCalledInController();
        thenVerifyDeleteLeaguePlayerByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteLeaguePlayersByLeagueIdAndPlayerNotFound() {
        whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsNotFoundException();
        andDeleteLeaguePlayerByLeagueIdAndUsernameIsCalledInController();
        thenVerifyDeleteLeaguePlayerByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteLeaguePlayersByLeagueIdAndPlayerServerError() {
        whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsServerException();
        andDeleteLeaguePlayerByLeagueIdAndUsernameIsCalledInController();
        thenVerifyDeleteLeaguePlayerByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void givenValidLeaguePlayersDTOForRequest(String username1, String username2) {
        leaguePlayersDTO = List.of(
                new LeaguePlayerDTO(1L, username1, 26, PlayerStatus.ACTIVE),
                new LeaguePlayerDTO(1L, username2, 12, PlayerStatus.ACTIVE)
        );
    }

    private void whenFindLeaguePlayersByLeagueIdInServiceReturnedValidLeaguePlayers() {
        expectedLeaguePlayers = List.of(
                new LeaguePlayerDTO(1L, "someUser", 10, PlayerStatus.ACTIVE),
                new LeaguePlayerDTO(1L, "otherUser", 10, PlayerStatus.ACTIVE)
        );

        when(leaguePlayersBOServiceMock.findLeaguePlayersByLeagueId(1L)).thenReturn(expectedLeaguePlayers);
    }

    private void whenFindLeaguePlayersByLeagueIdInServiceThrowsNotFoundException() {
        when(leaguePlayersBOServiceMock.findLeaguePlayersByLeagueId(1L))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayersByLeagueIdInServiceThrowsServerException() {
        when(leaguePlayersBOServiceMock.findLeaguePlayersByLeagueId(1L))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeaguePlayersByLeagueIdAndUsernameInServiceReturnedValidLeaguePlayers() {
        expectedLeaguePlayers = List.of(
                new LeaguePlayerDTO(1L, "someUser", 10, PlayerStatus.ACTIVE)
        );

        when(leaguePlayersBOServiceMock.findLeaguePlayerByByLeagueIdAndPlayer(1L, "someUser"))
                .thenReturn(expectedLeaguePlayers.getFirst());
    }

    private void whenFindLeaguePlayersByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        when(leaguePlayersBOServiceMock.findLeaguePlayerByByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayersByLeagueIdAndUsernameInServiceThrowsServerException() {
        when(leaguePlayersBOServiceMock.findLeaguePlayerByByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeaguesByUsernameInServiceReturnedValidLeaguePlayers() {
        expectedLeagues = List.of(
                new LeagueDTO("Some League", "Some description", "PREMIER",
                        false, "somePass", LeagueStatus.ACTIVE, 200,
                        null, false, "someUser", null),
                new LeagueDTO("Other League", "Other description", "PREMIER",
                        true, null, LeagueStatus.COMPLETED, 36,
                        null, false, "otherUser", null)
        );

        when(leaguePlayersBOServiceMock.findLeaguesByUsername("someUser")).thenReturn(expectedLeagues);
    }

    private void whenFindLeaguesByUsernameInServiceThrowsNotFoundException() {
        when(leaguePlayersBOServiceMock.findLeaguesByUsername("someUser"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguesByUsernameInServiceThrowsServerException() {
        when(leaguePlayersBOServiceMock.findLeaguesByUsername("someUser"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateLeaguePlayerInServiceReturnedAValidLeaguePlayer() {
        expectedLeaguePlayerIds = List.of(new LeaguePlayerId(1L, "someUser"),
                new LeaguePlayerId(1L, "otherUser"));

        when(leaguePlayersBOServiceMock.createLeaguePlayers(anyList()))
                .thenReturn(expectedLeaguePlayerIds);
    }

    private void whenCreateLeaguePlayerInServiceThrowsServerException() {
        when(leaguePlayersBOServiceMock.createLeaguePlayers(anyList()))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenAddPointsToLeaguePlayerInServiceIsOk() {
        doNothing().when(leaguePlayersBOServiceMock).addPointsToLeaguePlayer(anyLong(), anyString(), anyInt());
    }

    private void whenAddPointsToLeaguePlayerInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayersBOServiceMock)
                .addPointsToLeaguePlayer(anyLong(), anyString(), anyInt());
    }

    private void whenAddPointsToLeaguePlayerInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayersBOServiceMock)
                .addPointsToLeaguePlayer(anyLong(), anyString(), anyInt());
    }

    private void whenDeleteLeaguePlayerLeagueIdInServiceIsOk() {
        doNothing().when(leaguePlayersBOServiceMock).deleteLeaguePlayersByLeagueId(anyLong());
    }

    private void whenDeleteLeaguePlayerLeagueIdInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayersBOServiceMock)
                .deleteLeaguePlayersByLeagueId(anyLong());
    }

    private void whenDeleteLeaguePlayerLeagueIdInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayersBOServiceMock)
                .deleteLeaguePlayersByLeagueId(anyLong());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceIsOk() {
        doNothing().when(leaguePlayersBOServiceMock).deleteLeaguePlayersByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayersBOServiceMock)
                .deleteLeaguePlayersByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayersBOServiceMock)
                .deleteLeaguePlayersByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void andFindLeaguePlayersByLeagueIdIsCalledInController() {
        result = leaguePlayerBOController.findLeaguePlayersByLeagueId(this.requestUid, 1L);
    }

    private void andFindLeaguePlayersByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerBOController.findLeaguePlayerByLeagueIdAndPlayer(this.requestUid, 1L, "someUser");
    }

    private void andFindLeaguesByUsernameIsCalledInController() {
        result = leaguePlayerBOController.findLeaguePlayersByUsername(this.requestUid, "someUser");
    }

    private void andCreateLeaguePlayerIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = leaguePlayerBOController.createLeaguePlayers(this.requestUid, leaguePlayersDTO, bindingResult);
    }

    private void andAddPointsToLeaguePlayerIsCalledInController() {
        result = leaguePlayerBOController.addPointsToLeaguePlayer(this.requestUid, 1L, "somePlayer", 3);
    }

    private void andDeleteLeaguePlayerLeagueIdIsCalledInController() {
        result = leaguePlayerBOController.deleteLeaguePlayersByLeagueId(this.requestUid, 1L);
    }

    private void andDeleteLeaguePlayerByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerBOController.deleteLeaguePlayersByLeagueIdAndPlayer(this.requestUid, 1L, "somePlayer");
    }

    private void thenVerifyFindLeaguePlayersByLeagueIdHasBeenCalledInService() {
        verify(leaguePlayersBOServiceMock, times(1)).findLeaguePlayersByLeagueId(1L);
    }

    private void thenVerifyFindLeaguePlayersByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayersBOServiceMock, times(1))
                .findLeaguePlayerByByLeagueIdAndPlayer(1L, "someUser");
    }

    private void thenVerifyFindLeaguesByUsernameHasBeenCalledInService() {
        verify(leaguePlayersBOServiceMock, times(1)).findLeaguesByUsername("someUser");
    }

    private void thenVerifyCreateLeaguePlayerHasBeenCalledInService() {
        verify(leaguePlayersBOServiceMock, times(1))
                .createLeaguePlayers(anyList());
    }

    private void thenVerifyAddPointsToLeaguePlayerHasBeenCalledInService() {
        verify(leaguePlayersBOServiceMock, times(1))
                .addPointsToLeaguePlayer(anyLong(), anyString(), anyInt());
    }

    private void thenVerifyDeleteLeaguePlayerLeagueIdHasBeenCalledInService() {
        verify(leaguePlayersBOServiceMock, times(1))
                .deleteLeaguePlayersByLeagueId(anyLong());
    }

    private void thenVerifyDeleteLeaguePlayerByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayersBOServiceMock, times(1))
                .deleteLeaguePlayersByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsLeaguePlayerIdIs(List<LeaguePlayerId> expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

    private void thenReceivedResponseBodyAsLeaguePlayerListIs(List<LeaguePlayerDTO> expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

    private void thenReceivedResponseBodyAsLeagueListIs(List<LeagueDTO> expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

    private void thenReceivedResponseBodyAsLeaguePlayerIs(LeaguePlayerDTO expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }
}

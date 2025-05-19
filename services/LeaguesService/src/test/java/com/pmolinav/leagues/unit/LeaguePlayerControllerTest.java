package com.pmolinav.leagues.unit;

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

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LeaguePlayerControllerTest extends BaseUnitTest {

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
    void deleteLeaguePlayersByLeagueIdAndUsernameHappyPath() {
        whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceIsOk();
        andDeleteLeaguePlayerByLeagueIdAndUsernameIsCalledInController();
        thenVerifyDeleteLeaguePlayerByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteLeaguePlayersByLeagueIdAndUsernameNotFound() {
        whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsNotFoundException();
        andDeleteLeaguePlayerByLeagueIdAndUsernameIsCalledInController();
        thenVerifyDeleteLeaguePlayerByLeagueIdAndUsernameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteLeaguePlayersByLeagueIdAndUsernameServerError() {
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

        when(leaguePlayerServiceMock.findLeaguePlayersByLeagueId(1L)).thenReturn(expectedLeaguePlayers);
    }

    private void whenFindLeaguePlayersByLeagueIdInServiceThrowsNotFoundException() {
        when(leaguePlayerServiceMock.findLeaguePlayersByLeagueId(1L))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayersByLeagueIdInServiceThrowsServerException() {
        when(leaguePlayerServiceMock.findLeaguePlayersByLeagueId(1L))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeaguePlayersByLeagueIdAndUsernameInServiceReturnedValidLeaguePlayers() {
        expectedLeaguePlayers = List.of(
                new LeaguePlayerDTO(1L, "someUser", 10, PlayerStatus.ACTIVE)
        );

        when(leaguePlayerServiceMock.findLeaguePlayerByLeagueIdAndPlayer(1L, "someUser"))
                .thenReturn(expectedLeaguePlayers.getFirst());
    }

    private void whenFindLeaguePlayersByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        when(leaguePlayerServiceMock.findLeaguePlayerByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayersByLeagueIdAndUsernameInServiceThrowsServerException() {
        when(leaguePlayerServiceMock.findLeaguePlayerByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeaguesByUsernameInServiceReturnedValidLeaguePlayers() {
        expectedLeagues = List.of(
                new LeagueDTO("Some League", "Some description", "PREMIER",
                        false, "somePass", LeagueStatus.ACTIVE, 200,
                        null, false, "someUser"),
                new LeagueDTO("Other League", "Other description", "PREMIER",
                        true, null, LeagueStatus.COMPLETED, 36,
                        null, false, "otherUser")
        );

        when(leaguePlayerServiceMock.findLeaguesByUsername("someUser")).thenReturn(expectedLeagues);
    }

    private void whenFindLeaguesByUsernameInServiceThrowsNotFoundException() {
        when(leaguePlayerServiceMock.findLeaguesByUsername("someUser"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguesByUsernameInServiceThrowsServerException() {
        when(leaguePlayerServiceMock.findLeaguesByUsername("someUser"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateLeaguePlayerInServiceReturnedAValidLeaguePlayer() {
        expectedLeaguePlayerIds = List.of(new LeaguePlayerId(1L, "someUser"),
                new LeaguePlayerId(1L, "otherUser"));

        when(leaguePlayerServiceMock.createLeaguePlayers(anyList()))
                .thenReturn(expectedLeaguePlayerIds);
    }

    private void whenCreateLeaguePlayerInServiceThrowsServerException() {
        when(leaguePlayerServiceMock.createLeaguePlayers(anyList()))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenAddPointsToLeaguePlayerInServiceIsOk() {
        doNothing().when(leaguePlayerServiceMock).addPointsToLeaguePlayer(anyLong(), anyString(), anyInt());
    }

    private void whenAddPointsToLeaguePlayerInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayerServiceMock)
                .addPointsToLeaguePlayer(anyLong(), anyString(), anyInt());
    }

    private void whenAddPointsToLeaguePlayerInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayerServiceMock)
                .addPointsToLeaguePlayer(anyLong(), anyString(), anyInt());
    }

    private void whenDeleteLeaguePlayerLeagueIdInServiceIsOk() {
        doNothing().when(leaguePlayerServiceMock).deleteLeaguePlayersByLeagueId(anyLong());
    }

    private void whenDeleteLeaguePlayerLeagueIdInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayerServiceMock)
                .deleteLeaguePlayersByLeagueId(anyLong());
    }

    private void whenDeleteLeaguePlayerLeagueIdInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayerServiceMock)
                .deleteLeaguePlayersByLeagueId(anyLong());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceIsOk() {
        doNothing().when(leaguePlayerServiceMock).deleteLeaguePlayersByLeagueIdAndUsername(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayerServiceMock)
                .deleteLeaguePlayersByLeagueIdAndUsername(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayerServiceMock)
                .deleteLeaguePlayersByLeagueIdAndUsername(anyLong(), anyString());
    }

    private void andFindLeaguePlayersByLeagueIdIsCalledInController() {
        result = leaguePlayerController.findLeaguePlayersByLeagueId(1L);
    }

    private void andFindLeaguePlayersByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerController.findLeaguePlayerByLeagueIdAndPlayer(1L, "someUser");
    }

    private void andFindLeaguesByUsernameIsCalledInController() {
        result = leaguePlayerController.findLeaguePlayersByUsername("someUser");
    }

    private void andCreateLeaguePlayerIsCalledInController() {
        result = leaguePlayerController.createLeaguePlayers(leaguePlayersDTO);
    }

    private void andAddPointsToLeaguePlayerIsCalledInController() {
        result = leaguePlayerController.addPointsToLeaguePlayer(1L,"somePlayer",3);
    }

    private void andDeleteLeaguePlayerLeagueIdIsCalledInController() {
        result = leaguePlayerController.deleteLeaguePlayersByLeagueId(1L);
    }

    private void andDeleteLeaguePlayerByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerController.deleteLeaguePlayersByLeagueIdAndPlayer(1L, "somePlayer");
    }

    private void thenVerifyFindLeaguePlayersByLeagueIdHasBeenCalledInService() {
        verify(leaguePlayerServiceMock, times(1)).findLeaguePlayersByLeagueId(1L);
    }

    private void thenVerifyFindLeaguePlayersByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayerServiceMock, times(1))
                .findLeaguePlayerByLeagueIdAndPlayer(1L, "someUser");
    }

    private void thenVerifyFindLeaguesByUsernameHasBeenCalledInService() {
        verify(leaguePlayerServiceMock, times(1)).findLeaguesByUsername("someUser");
    }

    private void thenVerifyCreateLeaguePlayerHasBeenCalledInService() {
        verify(leaguePlayerServiceMock, times(1))
                .createLeaguePlayers(anyList());
    }

    private void thenVerifyAddPointsToLeaguePlayerHasBeenCalledInService() {
        verify(leaguePlayerServiceMock, times(1))
                .addPointsToLeaguePlayer(anyLong(),anyString(),anyInt());
    }

    private void thenVerifyDeleteLeaguePlayerLeagueIdHasBeenCalledInService() {
        verify(leaguePlayerServiceMock, times(1))
                .deleteLeaguePlayersByLeagueId(anyLong());
    }

    private void thenVerifyDeleteLeaguePlayerByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayerServiceMock, times(1))
                .deleteLeaguePlayersByLeagueIdAndUsername(anyLong(), anyString());
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

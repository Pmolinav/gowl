package com.pmolinav.league.units;

import com.pmolinav.league.exceptions.InternalServerErrorException;
import com.pmolinav.league.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import com.pmolinav.leagueslib.model.LeagueStatus;
import com.pmolinav.leagueslib.model.PlayerStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

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
        givenMockedSecurityContextWithUser("someUser");
        givenValidLeaguePlayersDTOForRequest("someUser");
        whenCreateLeaguePlayerInServiceReturnedAValidLeaguePlayer();
        andCreateLeaguePlayerIsCalledInController();
        thenVerifyCreateLeaguePlayerHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsLeaguePlayerIdIs(expectedLeaguePlayerIds);
    }

    @Test
    void createLeaguePlayerServerError() {
        givenMockedSecurityContextWithUser("someUser");
        givenValidLeaguePlayersDTOForRequest("someUser");
        whenCreateLeaguePlayerInServiceThrowsServerException();
        andCreateLeaguePlayerIsCalledInController();
        thenVerifyCreateLeaguePlayerHasBeenCalledInService();
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

    private void givenMockedSecurityContextWithUser(String username) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(username, null, List.of());

        SecurityContext context = Mockito.mock(SecurityContext.class);

        Mockito.when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    private void givenValidLeaguePlayersDTOForRequest(String username1) {
        leaguePlayersDTO = List.of(
                new LeaguePlayerDTO(1L, username1, 26, PlayerStatus.ACTIVE)
        );
    }

    private void whenFindLeaguePlayersByLeagueIdInServiceReturnedValidLeaguePlayers() {
        expectedLeaguePlayers = List.of(
                new LeaguePlayerDTO(1L, "someUser", 10, PlayerStatus.ACTIVE),
                new LeaguePlayerDTO(1L, "otherUser", 10, PlayerStatus.ACTIVE)
        );

        when(leaguePlayersServiceMock.findLeaguePlayersByLeagueId(1L)).thenReturn(expectedLeaguePlayers);
    }

    private void whenFindLeaguePlayersByLeagueIdInServiceThrowsNotFoundException() {
        when(leaguePlayersServiceMock.findLeaguePlayersByLeagueId(1L))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayersByLeagueIdInServiceThrowsServerException() {
        when(leaguePlayersServiceMock.findLeaguePlayersByLeagueId(1L))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeaguePlayersByLeagueIdAndUsernameInServiceReturnedValidLeaguePlayers() {
        expectedLeaguePlayers = List.of(
                new LeaguePlayerDTO(1L, "someUser", 10, PlayerStatus.ACTIVE)
        );

        when(leaguePlayersServiceMock.findLeaguePlayerByByLeagueIdAndPlayer(1L, "someUser"))
                .thenReturn(expectedLeaguePlayers.getFirst());
    }

    private void whenFindLeaguePlayersByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        when(leaguePlayersServiceMock.findLeaguePlayerByByLeagueIdAndPlayer(1L, "someUser"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguePlayersByLeagueIdAndUsernameInServiceThrowsServerException() {
        when(leaguePlayersServiceMock.findLeaguePlayerByByLeagueIdAndPlayer(1L, "someUser"))
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

        when(leaguePlayersServiceMock.findLeaguesByUsername("someUser")).thenReturn(expectedLeagues);
    }

    private void whenFindLeaguesByUsernameInServiceThrowsNotFoundException() {
        when(leaguePlayersServiceMock.findLeaguesByUsername("someUser"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeaguesByUsernameInServiceThrowsServerException() {
        when(leaguePlayersServiceMock.findLeaguesByUsername("someUser"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateLeaguePlayerInServiceReturnedAValidLeaguePlayer() {
        expectedLeaguePlayerIds = List.of(new LeaguePlayerId(1L, "someUser"),
                new LeaguePlayerId(1L, "otherUser"));

        when(leaguePlayersServiceMock.createLeaguePlayers(anyList()))
                .thenReturn(expectedLeaguePlayerIds);
    }

    private void whenCreateLeaguePlayerInServiceThrowsServerException() {
        when(leaguePlayersServiceMock.createLeaguePlayers(anyList()))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceIsOk() {
        doNothing().when(leaguePlayersServiceMock).deleteLeaguePlayersByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguePlayersServiceMock)
                .deleteLeaguePlayersByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void whenDeleteLeaguePlayerByLeagueIdAndUsernameInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leaguePlayersServiceMock)
                .deleteLeaguePlayersByLeagueIdAndPlayer(anyLong(), anyString());
    }

    private void andFindLeaguePlayersByLeagueIdIsCalledInController() {
        result = leaguePlayerController.findLeaguePlayersByLeagueId(this.requestUid, 1L);
    }

    private void andFindLeaguePlayersByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerController.findLeaguePlayerByLeagueIdAndPlayer(this.requestUid, 1L, "someUser");
    }

    private void andFindLeaguesByUsernameIsCalledInController() {
        result = leaguePlayerController.findLeaguePlayersByUsername(this.requestUid, "someUser");
    }

    private void andCreateLeaguePlayerIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = leaguePlayerController.createLeaguePlayers(this.requestUid, leaguePlayersDTO, bindingResult);
    }

    private void andDeleteLeaguePlayerByLeagueIdAndUsernameIsCalledInController() {
        result = leaguePlayerController.deleteLeaguePlayersByLeagueIdAndPlayer(this.requestUid, 1L, "somePlayer");
    }

    private void thenVerifyFindLeaguePlayersByLeagueIdHasBeenCalledInService() {
        verify(leaguePlayersServiceMock, times(1)).findLeaguePlayersByLeagueId(1L);
    }

    private void thenVerifyFindLeaguePlayersByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayersServiceMock, times(1))
                .findLeaguePlayerByByLeagueIdAndPlayer(1L, "someUser");
    }

    private void thenVerifyFindLeaguesByUsernameHasBeenCalledInService() {
        verify(leaguePlayersServiceMock, times(1)).findLeaguesByUsername("someUser");
    }

    private void thenVerifyCreateLeaguePlayerHasBeenCalledInService() {
        verify(leaguePlayersServiceMock, times(1))
                .createLeaguePlayers(anyList());
    }

    private void thenVerifyAddPointsToLeaguePlayerHasBeenCalledInService() {
        verify(leaguePlayersServiceMock, times(1))
                .addPointsToLeaguePlayer(anyLong(), anyString(), anyInt());
    }

    private void thenVerifyDeleteLeaguePlayerLeagueIdHasBeenCalledInService() {
        verify(leaguePlayersServiceMock, times(1))
                .deleteLeaguePlayersByLeagueId(anyLong());
    }

    private void thenVerifyDeleteLeaguePlayerByLeagueIdAndUsernameHasBeenCalledInService() {
        verify(leaguePlayersServiceMock, times(1))
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

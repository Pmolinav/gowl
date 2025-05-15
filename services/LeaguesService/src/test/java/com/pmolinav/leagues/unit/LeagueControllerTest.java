package com.pmolinav.leagues.unit;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.model.League;
import com.pmolinav.leagueslib.model.LeaguePlayerId;
import com.pmolinav.leagueslib.model.LeagueStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LeagueControllerTest extends BaseUnitTest {

    LeagueDTO leagueDTO;
    List<LeagueDTO> expectedLeagues;
    ResponseEntity<?> result;

    /* FIND ALL LEAGUES */
    @Test
    void findAllLeaguesHappyPath() {
        whenFindAllLeaguesInServiceReturnedValidLeagues();
        andFindAllLeaguesIsCalledInController();
        thenVerifyFindAllLeaguesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsLeagueListIs(expectedLeagues);
    }

    @Test
    void findAllLeaguesNotFound() {
        whenFindAllLeaguesInServiceThrowsNotFoundException();
        andFindAllLeaguesIsCalledInController();
        thenVerifyFindAllLeaguesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllLeaguesServerError() {
        whenFindAllLeaguesInServiceThrowsServerException();
        andFindAllLeaguesIsCalledInController();
        thenVerifyFindAllLeaguesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CREATE LEAGUE */
    @Test
    void createLeagueHappyPath() {
        givenValidLeagueDTOForRequest("Some League", "Some description", "PREMIER");
        whenCreateLeagueInServiceReturnedAValidLeague();
        whenCreateLeaguePlayerInServiceReturnedAValidLeaguePlayerId();
        andCreateLeagueIsCalledInController();
        thenVerifyCreateLeagueHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsLeagueIdIs(1L);
    }

    @Test
    void createLeagueServerError() {
        givenValidLeagueDTOForRequest("Other League", "Other description", "PREMIER2");
        whenCreateLeagueInServiceThrowsServerException();
        andCreateLeagueIsCalledInController();
        thenVerifyCreateLeagueHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND LEAGUES BY LEAGUE ID */
    @Test
    void findLeagueByLeagueIdHappyPath() {
        whenFindLeagueByLeagueIdInServiceReturnedValidLeague();
        andFindLeagueByLeagueIdIsCalledInController();
        thenVerifyFindByLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsLeagueIs(expectedLeagues.getFirst());
    }

    @Test
    void findLeagueByLeagueIdNotFound() {
        whenFindLeagueByLeagueIdInServiceThrowsNotFoundException();
        andFindLeagueByLeagueIdIsCalledInController();
        thenVerifyFindByLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeagueByLeagueIdServerError() {
        whenFindLeagueByLeagueIdInServiceThrowsServerException();
        andFindLeagueByLeagueIdIsCalledInController();
        thenVerifyFindByLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND LEAGUES BY NAME */
    @Test
    void findLeagueByNameHappyPath() {
        whenFindLeagueByNameInServiceReturnedValidLeague();
        andFindLeagueByNameIsCalledInController();
        thenVerifyFindByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsLeagueIs(expectedLeagues.getFirst());
    }

    @Test
    void findLeagueByNameNotFound() {
        whenFindLeagueByNameInServiceThrowsNotFoundException();
        andFindLeagueByNameIsCalledInController();
        thenVerifyFindByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeaguesByNameServerError() {
        whenFindLeagueByNameInServiceThrowsServerException();
        andFindLeagueByNameIsCalledInController();
        thenVerifyFindByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE LEAGUES BY LEAGUE ID*/
    @Test
    void deleteLeaguesByLeagueIdHappyPath() {
        whenDeleteLeagueByLeagueIdInServiceIsOk();
        andDeleteLeagueByLeagueIdIsCalledInController();
        thenVerifyDeleteLeagueByLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteLeaguesByLeagueIdNotFound() {
        whenDeleteLeagueByLeagueIdInServiceThrowsNotFoundException();
        andDeleteLeagueByLeagueIdIsCalledInController();
        thenVerifyDeleteLeagueByLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteLeaguesByLeagueIdServerError() {
        whenDeleteLeagueByLeagueIdInServiceThrowsServerException();
        andDeleteLeagueByLeagueIdIsCalledInController();
        thenVerifyDeleteLeagueByLeagueIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE LEAGUE BY NAME*/
    @Test
    void deleteLeaguesByNameHappyPath() {
        whenDeleteLeagueByNameInServiceIsOk();
        andDeleteLeagueByNameIsCalledInController();
        thenVerifyDeleteLeagueByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteLeaguesByNameNotFound() {
        whenDeleteLeagueByNameInServiceThrowsNotFoundException();
        andDeleteLeagueByNameIsCalledInController();
        thenVerifyDeleteLeagueByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteLeaguesByNameServerError() {
        whenDeleteLeagueByNameInServiceThrowsServerException();
        andDeleteLeagueByNameIsCalledInController();
        thenVerifyDeleteLeagueByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void givenValidLeagueDTOForRequest(String name, String description, String categoryId) {
        leagueDTO = new LeagueDTO(name, description, categoryId, false,
                "somePassword", LeagueStatus.ACTIVE, 26, null,
                false, "someUser");
    }

    private void whenFindAllLeaguesInServiceReturnedValidLeagues() {
        expectedLeagues = List.of(
                new LeagueDTO("Some League", "Some description", "PREMIER",
                        false, "somePass", LeagueStatus.ACTIVE, 200,
                        null, false, "someUser"),
                new LeagueDTO("Other League", "Other description", "PREMIER",
                        true, null, LeagueStatus.COMPLETED, 36,
                        null, false, "otherUser")
        );

        when(leagueServiceMock.findAllLeagues()).thenReturn(expectedLeagues);
    }

    private void whenFindAllLeaguesInServiceThrowsNotFoundException() {
        when(leagueServiceMock.findAllLeagues()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllLeaguesInServiceThrowsServerException() {
        when(leagueServiceMock.findAllLeagues())
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateLeagueInServiceReturnedAValidLeague() {
        when(leagueServiceMock.createLeague(any()))
                .thenReturn(new League(1L, "Some League", "Some description",
                        "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                        200, null, false, "someUser",
                        12345L, null));
    }

    private void whenCreateLeaguePlayerInServiceReturnedAValidLeaguePlayerId() {
        when(leaguePlayerServiceMock.createLeaguePlayers(anyList()))
                .thenReturn(List.of(new LeaguePlayerId(1L, "someUser")));
    }

    private void whenCreateLeagueInServiceThrowsServerException() {
        when(leagueServiceMock.createLeague(any(LeagueDTO.class)))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeagueByLeagueIdInServiceReturnedValidLeague() {
        expectedLeagues = List.of(new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                200, null, false, "someUser"));

        when(leagueServiceMock.findById(1L)).thenReturn(expectedLeagues.getFirst());
    }

    private void whenFindLeagueByLeagueIdInServiceThrowsNotFoundException() {
        when(leagueServiceMock.findById(1L)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeagueByLeagueIdInServiceThrowsServerException() {
        when(leagueServiceMock.findById(1L))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeagueByNameInServiceReturnedValidLeague() {
        expectedLeagues = List.of(new LeagueDTO("Some League", "Some description",
                "PREMIER", false, "somePass", LeagueStatus.ACTIVE,
                200, null, false, "someUser"));

        when(leagueServiceMock.findByName("Some League")).thenReturn(expectedLeagues.getFirst());
    }

    private void whenFindLeagueByNameInServiceThrowsNotFoundException() {
        when(leagueServiceMock.findByName("Some League")).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeagueByNameInServiceThrowsServerException() {
        when(leagueServiceMock.findByName("Some League"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenDeleteLeagueByLeagueIdInServiceIsOk() {
        doNothing().when(leagueServiceMock).deleteLeague(anyLong());
    }

    private void whenDeleteLeagueByLeagueIdInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leagueServiceMock)
                .deleteLeague(anyLong());
    }

    private void whenDeleteLeagueByLeagueIdInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leagueServiceMock)
                .deleteLeague(anyLong());
    }

    private void whenDeleteLeagueByNameInServiceIsOk() {
        doNothing().when(leagueServiceMock).deleteLeagueByName(anyString());
    }

    private void whenDeleteLeagueByNameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leagueServiceMock)
                .deleteLeagueByName(anyString());
    }

    private void whenDeleteLeagueByNameInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leagueServiceMock)
                .deleteLeagueByName(anyString());
    }

    private void andFindAllLeaguesIsCalledInController() {
        result = leagueController.findAllLeagues();
    }

    private void andFindLeagueByLeagueIdIsCalledInController() {
        result = leagueController.findLeagueById(1L);
    }

    private void andFindLeagueByNameIsCalledInController() {
        result = leagueController.findLeagueByName("Some League");
    }

    private void andCreateLeagueIsCalledInController() {
        result = leagueController.createLeague(leagueDTO);
    }

    private void andDeleteLeagueByLeagueIdIsCalledInController() {
        result = leagueController.deleteLeague(1L);
    }

    private void andDeleteLeagueByNameIsCalledInController() {
        result = leagueController.deleteLeagueByName("Some League");
    }

    private void thenVerifyFindAllLeaguesHasBeenCalledInService() {
        verify(leagueServiceMock, times(1)).findAllLeagues();
    }

    private void thenVerifyCreateLeagueHasBeenCalledInService() {
        verify(leagueServiceMock, times(1))
                .createLeague(any(LeagueDTO.class));
    }

    private void thenVerifyFindByLeagueIdHasBeenCalledInService() {
        verify(leagueServiceMock, times(1)).findById(anyLong());
    }

    private void thenVerifyFindByNameHasBeenCalledInService() {
        verify(leagueServiceMock, times(1)).findByName(anyString());
    }

    private void thenVerifyDeleteLeagueByLeagueIdHasBeenCalledInService() {
        verify(leagueServiceMock, times(1))
                .deleteLeague(anyLong());
    }

    private void thenVerifyDeleteLeagueByNameHasBeenCalledInService() {
        verify(leagueServiceMock, times(1))
                .deleteLeagueByName(anyString());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsLeagueIdIs(Long expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

    private void thenReceivedResponseBodyAsLeagueListIs(List<LeagueDTO> expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

    private void thenReceivedResponseBodyAsLeagueIs(LeagueDTO expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }
}

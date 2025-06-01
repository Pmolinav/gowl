package com.pmolinav.leagues.units;

import com.pmolinav.leagues.exceptions.CustomStatusException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.model.LeagueStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class LeagueBOControllerTest extends BaseUnitTest {

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
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedLeagues));
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

    /* FIND LEAGUE BY ID */
    @Test
    void findLeagueByIdHappyPath() {
        whenFindLeagueByIdInServiceReturnedValidLeague();
        andFindLeagueByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedLeagues.getFirst()));
    }

    @Test
    void findLeagueByIdNotFound() {
        whenFindLeagueByIdInServiceThrowsNotFoundException();
        andFindLeagueByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeagueByIdServerError() {
        whenFindLeagueByIdInServiceThrowsServerException();
        andFindLeagueByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND LEAGUE BY NAME */
    @Test
    void findLeagueByNameHappyPath() {
        whenFindLeagueByNameInServiceReturnedValidLeague();
        andFindLeagueByNameIsCalledInController();
        thenVerifyFindByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedLeagues.getFirst()));
    }

    @Test
    void findLeagueByNameNotFound() {
        whenFindLeagueByNameInServiceThrowsNotFoundException();
        andFindLeagueByNameIsCalledInController();
        thenVerifyFindByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeagueByNameServerError() {
        whenFindLeagueByNameInServiceThrowsServerException();
        andFindLeagueByNameIsCalledInController();
        thenVerifyFindByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CREATE LEAGUE */
    @Test
    void createLeagueHappyPath() {
        givenValidLeagueDTOForRequest("Some League", "Some Description", "PREMIER");
        whenCreateLeagueInServiceReturnedAValidLeague();
        andCreateLeagueIsCalledInController();
        thenVerifyCreateLeagueHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsStringIs(String.valueOf(1));
    }

    @Test
    void createLeagueBadRequest() {
        givenValidLeagueDTOForRequest("Other League", "Other Description", "someCategory");
        whenCreateLeagueInServiceReturnedAValidLeague();
        andCreateLeagueIsCalledInControllerWithBindingResultErrors();
        thenReceivedStatusCodeIs(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createLeagueServerError() {
        givenValidLeagueDTOForRequest("Another League", "Another Description", "PREMIER");
        whenCreateLeagueInServiceThrowsServerException();
        andCreateLeagueIsCalledInController();
        thenVerifyCreateLeagueHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CLOSE LEAGUE BY ID */
    @Test
    void closeLeagueByIdHappyPath() {
        whenCloseLeagueByIdInServiceReturnedAValidLeague();
        andCloseLeagueByIdIsCalledInController();
        thenVerifyCloseLeagueByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void closeLeagueByIdServerError() {
        whenCloseLeagueByIdInServiceThrowsServerException();
        andCloseLeagueByIdIsCalledInController();
        thenVerifyCloseLeagueByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CLOSE LEAGUE BY NAME */
    @Test
    void closeLeagueByNameHappyPath() {
        whenCloseLeagueByNameInServiceReturnedAValidLeague();
        andCloseLeagueByNameIsCalledInController();
        thenVerifyCloseLeagueByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void closeLeagueByNameServerError() {
        whenCloseLeagueByNameInServiceThrowsServerException();
        andCloseLeagueByNameIsCalledInController();
        thenVerifyCloseLeagueByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE LEAGUE BY ID */
    @Test
    void deleteLeagueHappyPath() {
        whenDeleteLeagueInServiceIsOk();
        andDeleteLeagueIsCalledInController();
        thenVerifyDeleteLeagueHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteLeagueNotFound() {
        whenDeleteLeagueInServiceThrowsNotFoundException();
        andDeleteLeagueIsCalledInController();
        thenVerifyDeleteLeagueHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteLeagueServerError() {
        whenDeleteLeagueInServiceThrowsServerException();
        andDeleteLeagueIsCalledInController();
        thenVerifyDeleteLeagueHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE LEAGUE BY NAME*/
    @Test
    void deleteLeagueByNameHappyPath() {
        whenDeleteLeagueByNameInServiceIsOk();
        andDeleteLeagueByNameIsCalledInController();
        thenVerifyDeleteLeagueByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteLeagueByNameNotFound() {
        whenDeleteLeagueByNameInServiceThrowsNotFoundException();
        andDeleteLeagueByNameIsCalledInController();
        thenVerifyDeleteLeagueByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteLeagueByNameServerError() {
        whenDeleteLeagueByNameInServiceThrowsServerException();
        andDeleteLeagueByNameIsCalledInController();
        thenVerifyDeleteLeagueByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void givenValidLeagueDTOForRequest(String name, String description, String categoryId) {
        leagueDTO = new LeagueDTO(name, description, categoryId, false,
                "somePassword", LeagueStatus.ACTIVE, 26, null,
                false, "someUser", null);
    }

    private void whenFindAllLeaguesInServiceReturnedValidLeagues() {
        expectedLeagues = List.of(
                new LeagueDTO("Some League", "Some description", "PREMIER",
                        false, "somePass", LeagueStatus.ACTIVE, 200,
                        null, false, "someUser", null),
                new LeagueDTO("Other League", "Other description", "PREMIER",
                        true, null, LeagueStatus.COMPLETED, 36,
                        null, false, "otherUser", null)
        );

        when(leaguesBOServiceMock.findAllLeagues()).thenReturn(expectedLeagues);
    }

    private void whenFindAllLeaguesInServiceThrowsNotFoundException() {
        when(leaguesBOServiceMock.findAllLeagues()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllLeaguesInServiceThrowsServerException() {
        when(leaguesBOServiceMock.findAllLeagues())
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenCreateLeagueInServiceReturnedAValidLeague() {
        when(leaguesBOServiceMock.createLeague(any())).thenReturn(1L);
    }

    private void whenCloseLeagueByIdInServiceReturnedAValidLeague() {
        doNothing().when(leaguesBOServiceMock).closeLeagueById(anyLong());
    }

    private void whenCloseLeagueByNameInServiceReturnedAValidLeague() {
        doNothing().when(leaguesBOServiceMock).closeLeagueByName(anyString());
    }

    private void whenCreateLeagueInServiceThrowsServerException() {
        when(leaguesBOServiceMock.createLeague(any(LeagueDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenCloseLeagueByIdInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(leaguesBOServiceMock).closeLeagueById(anyLong());
    }

    private void whenCloseLeagueByNameInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(leaguesBOServiceMock).closeLeagueByName(anyString());
    }

    private void whenFindLeagueByIdInServiceReturnedValidLeague() {
        expectedLeagues = List.of(
                new LeagueDTO("Some League", "Some description", "PREMIER",
                        false, "somePass", LeagueStatus.ACTIVE, 200,
                        null, false, "someUser", null)
        );

        when(leaguesBOServiceMock.findLeagueById(1L)).thenReturn(expectedLeagues.getFirst());
    }

    private void whenFindLeagueByIdInServiceThrowsNotFoundException() {
        when(leaguesBOServiceMock.findLeagueById(1L)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeagueByIdInServiceThrowsServerException() {
        when(leaguesBOServiceMock.findLeagueById(1L))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindLeagueByNameInServiceReturnedValidLeague() {
        expectedLeagues = List.of(new LeagueDTO("Some League", "Some description", "PREMIER",
                false, "somePass", LeagueStatus.ACTIVE, 200,
                null, false, "someUser", null));

        when(leaguesBOServiceMock.findLeagueByName(anyString())).thenReturn(expectedLeagues.getFirst());
    }

    private void whenFindLeagueByNameInServiceThrowsNotFoundException() {
        when(leaguesBOServiceMock.findLeagueByName("Some League")).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeagueByNameInServiceThrowsServerException() {
        when(leaguesBOServiceMock.findLeagueByName("Some League"))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenDeleteLeagueInServiceIsOk() {
        doNothing().when(leaguesBOServiceMock).deleteLeague(anyLong());
    }

    private void whenDeleteLeagueInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguesBOServiceMock)
                .deleteLeague(anyLong());
    }

    private void whenDeleteLeagueInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(leaguesBOServiceMock)
                .deleteLeague(anyLong());
    }

    private void whenDeleteLeagueByNameInServiceIsOk() {
        doNothing().when(leaguesBOServiceMock).deleteLeagueByName(anyString());
    }

    private void whenDeleteLeagueByNameInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leaguesBOServiceMock)
                .deleteLeagueByName(anyString());
    }

    private void whenDeleteLeagueByNameInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(leaguesBOServiceMock)
                .deleteLeagueByName(anyString());
    }

    private void andFindAllLeaguesIsCalledInController() {
        result = leagueBOController.findAllLeagues(this.requestUid);
    }

    private void andFindLeagueByIdIsCalledInController() {
        result = leagueBOController.getLeagueById(this.requestUid, 1L);
    }

    private void andFindLeagueByNameIsCalledInController() {
        result = leagueBOController.getLeagueByName(this.requestUid, "Some League");
    }

    private void andCreateLeagueIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = leagueBOController.createLeague(this.requestUid, leagueDTO, bindingResult);
    }

    private void andCreateLeagueIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("userDTO", "username", "Name is mandatory.")
        ));

        result = leagueBOController.createLeague(this.requestUid, leagueDTO, bindingResult);
    }

    private void andCloseLeagueByIdIsCalledInController() {
        result = leagueBOController.closeLeagueById(this.requestUid, 1L);
    }

    private void andCloseLeagueByNameIsCalledInController() {
        result = leagueBOController.closeLeagueByName(this.requestUid, "Some League");
    }

    private void andDeleteLeagueIsCalledInController() {
        result = leagueBOController.deleteLeague(this.requestUid, 1L);
    }

    private void andDeleteLeagueByNameIsCalledInController() {
        result = leagueBOController.deleteLeagueByName(this.requestUid, "Some League");
    }

    private void thenVerifyFindAllLeaguesHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).findAllLeagues();
    }

    private void thenVerifyCreateLeagueHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).createLeague(any(LeagueDTO.class));
    }

    private void thenVerifyCloseLeagueByIdHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).closeLeagueById(anyLong());
    }

    private void thenVerifyCloseLeagueByNameHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).closeLeagueByName(anyString());
    }

    private void thenVerifyFindByIdHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).findLeagueById(anyLong());
    }

    private void thenVerifyFindByNameHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).findLeagueByName(anyString());
    }

    private void thenVerifyDeleteLeagueHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).deleteLeague(anyLong());
    }

    private void thenVerifyDeleteLeagueByNameHasBeenCalledInService() {
        verify(leaguesBOServiceMock, times(1)).deleteLeagueByName(anyString());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, String.valueOf(result.getBody()));
    }
}

package com.pmolinav.league.units;

import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.NotFoundException;
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

class LeagueControllerTest extends BaseUnitTest {

    LeagueDTO leagueDTO;
    List<LeagueDTO> expectedLeagues;
    ResponseEntity<?> result;

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

    private void givenValidLeagueDTOForRequest(String name, String description, String categoryId) {
        leagueDTO = new LeagueDTO(name, description, categoryId, false,
                "somePassword", LeagueStatus.ACTIVE, 26, null,
                false, "someUser", null);
    }

    private void whenCreateLeagueInServiceReturnedAValidLeague() {
        when(leaguesServiceMock.createLeague(any())).thenReturn(1L);
    }

    private void whenCloseLeagueByIdInServiceReturnedAValidLeague() {
        doNothing().when(leaguesServiceMock).closeLeagueById(anyLong());
    }

    private void whenCloseLeagueByNameInServiceReturnedAValidLeague() {
        doNothing().when(leaguesServiceMock).closeLeagueByName(anyString());
    }

    private void whenCreateLeagueInServiceThrowsServerException() {
        when(leaguesServiceMock.createLeague(any(LeagueDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenCloseLeagueByIdInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(leaguesServiceMock).closeLeagueById(anyLong());
    }

    private void whenCloseLeagueByNameInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(leaguesServiceMock).closeLeagueByName(anyString());
    }

    private void whenFindLeagueByIdInServiceReturnedValidLeague() {
        expectedLeagues = List.of(
                new LeagueDTO("Some League", "Some description", "PREMIER",
                        false, "somePass", LeagueStatus.ACTIVE, 200,
                        null, false, "someUser", null)
        );

        when(leaguesServiceMock.findLeagueById(1L)).thenReturn(expectedLeagues.getFirst());
    }

    private void whenFindLeagueByIdInServiceThrowsNotFoundException() {
        when(leaguesServiceMock.findLeagueById(1L)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeagueByIdInServiceThrowsServerException() {
        when(leaguesServiceMock.findLeagueById(1L))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindLeagueByNameInServiceReturnedValidLeague() {
        expectedLeagues = List.of(new LeagueDTO("Some League", "Some description", "PREMIER",
                false, "somePass", LeagueStatus.ACTIVE, 200,
                null, false, "someUser", null));

        when(leaguesServiceMock.findLeagueByName(anyString())).thenReturn(expectedLeagues.getFirst());
    }

    private void whenFindLeagueByNameInServiceThrowsNotFoundException() {
        when(leaguesServiceMock.findLeagueByName("Some League")).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeagueByNameInServiceThrowsServerException() {
        when(leaguesServiceMock.findLeagueByName("Some League"))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void andFindLeagueByIdIsCalledInController() {
        result = leagueController.getLeagueById(this.requestUid, 1L);
    }

    private void andFindLeagueByNameIsCalledInController() {
        result = leagueController.getLeagueByName(this.requestUid, "Some League");
    }

    private void andCreateLeagueIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = leagueController.createLeague(this.requestUid, leagueDTO, bindingResult);
    }

    private void andCreateLeagueIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("userDTO", "username", "Name is mandatory.")
        ));

        result = leagueController.createLeague(this.requestUid, leagueDTO, bindingResult);
    }

    private void andCloseLeagueByIdIsCalledInController() {
        result = leagueController.closeLeagueById(this.requestUid, 1L);
    }

    private void andCloseLeagueByNameIsCalledInController() {
        result = leagueController.closeLeagueByName(this.requestUid, "Some League");
    }

    private void thenVerifyCreateLeagueHasBeenCalledInService() {
        verify(leaguesServiceMock, times(1)).createLeague(any(LeagueDTO.class));
    }

    private void thenVerifyCloseLeagueByIdHasBeenCalledInService() {
        verify(leaguesServiceMock, times(1)).closeLeagueById(anyLong());
    }

    private void thenVerifyCloseLeagueByNameHasBeenCalledInService() {
        verify(leaguesServiceMock, times(1)).closeLeagueByName(anyString());
    }

    private void thenVerifyFindByIdHasBeenCalledInService() {
        verify(leaguesServiceMock, times(1)).findLeagueById(anyLong());
    }

    private void thenVerifyFindByNameHasBeenCalledInService() {
        verify(leaguesServiceMock, times(1)).findLeagueByName(anyString());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, String.valueOf(result.getBody()));
    }
}

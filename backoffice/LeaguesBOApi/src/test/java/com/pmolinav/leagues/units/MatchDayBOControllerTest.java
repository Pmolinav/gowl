package com.pmolinav.leagues.units;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.model.MatchDayId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MatchDayBOControllerTest extends BaseUnitTest {

    MatchDayDTO matchDayDTO;
    List<MatchDayDTO> expectedMatchDay;
    ResponseEntity<?> result;

    /* FIND ALL MATCH DAYS */
    @Test
    void findAllMatchDaysHappyPath() {
        whenFindAllMatchDaysInServiceReturnedValidMatchDays();
        andFindAllMatchDaysIsCalledInController();
        thenVerifyFindAllMatchDaysHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsMatchDayListIs(expectedMatchDay);
    }

    @Test
    void findAllMatchDaysNotFound() {
        whenFindAllMatchDaysInServiceThrowsNotFoundException();
        andFindAllMatchDaysIsCalledInController();
        thenVerifyFindAllMatchDaysHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllMatchDaysServerError() {
        whenFindAllMatchDaysInServiceThrowsServerException();
        andFindAllMatchDaysIsCalledInController();
        thenVerifyFindAllMatchDaysHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CREATE MATCH DAY */
    @Test
    void createMatchDayHappyPath() {
        givenValidMatchDayDTOForRequest("PREMIER", 2025, 10);
        whenCreateMatchDayInServiceReturnedAValidMatchDay();
        andCreateMatchDayIsCalledInController();
        thenVerifyCreateMatchDayHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsMatchDayIdIs(new MatchDayId("PREMIER", 2025, 10));
    }

    @Test
    void createMatchDayServerError() {
        givenValidMatchDayDTOForRequest("PREMIER", 2024, 33);
        whenCreateMatchDayInServiceThrowsServerException();
        andCreateMatchDayIsCalledInController();
        thenVerifyCreateMatchDayHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND MATCH DAYS BY CATEGORY ID */
    @Test
    void findMatchDaysByCategoryIdHappyPath() {
        whenFindMatchDayByCategoryIdInServiceReturnedValidMatchDays();
        andFindMatchDaysByCategoryIdIsCalledInController();
        thenVerifyfindMatchDaysByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsMatchDayListIs(expectedMatchDay);
    }

    @Test
    void findMatchDaysCategoryByIdNotFound() {
        whenFindMatchDaysByCategoryIdInServiceThrowsNotFoundException();
        andFindMatchDaysByCategoryIdIsCalledInController();
        thenVerifyfindMatchDaysByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findMatchDaysByCategoryIdServerError() {
        whenFindMatchDaysByCategoryIdInServiceThrowsServerException();
        andFindMatchDaysByCategoryIdIsCalledInController();
        thenVerifyfindMatchDaysByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND MATCH DAYS BY CATEGORY ID AND SEASON */
    @Test
    void findMatchDaysByCategoryIdAndSeasonHappyPath() {
        whenFindMatchDayByCategoryIdAndSeasonInServiceReturnedValidMatchDays();
        andFindMatchDaysByCategoryIdAndSeasonIsCalledInController();
        thenVerifyfindMatchDaysByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsMatchDayListIs(expectedMatchDay);
    }

    @Test
    void findMatchDaysCategoryByIdAndSeasonNotFound() {
        whenFindMatchDaysByCategoryIdAndSeasonInServiceThrowsNotFoundException();
        andFindMatchDaysByCategoryIdAndSeasonIsCalledInController();
        thenVerifyfindMatchDaysByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findMatchDaysByCategoryIdAndSeasonServerError() {
        whenFindMatchDaysByCategoryIdAndSeasonInServiceThrowsServerException();
        andFindMatchDaysByCategoryIdAndSeasonIsCalledInController();
        thenVerifyfindMatchDaysByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE MATCH DAYS BY CATEGORY ID*/
    @Test
    void deleteMatchDaysByCategoryIdHappyPath() {
        whenDeleteMatchDaysByCategoryIdInServiceIsOk();
        andDeleteMatchDaysByCategoryIdIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteMatchDaysByCategoryIdNotFound() {
        whenDeleteMatchDaysByCategoryIdInServiceThrowsNotFoundException();
        andDeleteMatchDaysByCategoryIdIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteMatchDaysByCategoryIdServerError() {
        whenDeleteMatchDaysByCategoryIdInServiceThrowsServerException();
        andDeleteMatchDaysByCategoryIdIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE MATCH DAY BY CATEGORY ID AND SEASON*/
    @Test
    void deleteMatchDaysByCategoryIdAndSeasonHappyPath() {
        whenDeleteMatchDaysByCategoryIdAndSeasonInServiceIsOk();
        andDeleteMatchDaysByCategoryIdAndSeasonIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteMatchDaysByCategoryIdAndSeasonNotFound() {
        whenDeleteMatchDaysByCategoryIdAndSeasonInServiceThrowsNotFoundException();
        andDeleteMatchDaysByCategoryIdAndSeasonIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteMatchDaysByCategoryIdAndSeasonServerError() {
        whenDeleteMatchDaysByCategoryIdAndSeasonInServiceThrowsServerException();
        andDeleteMatchDaysByCategoryIdAndSeasonIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE MATCH DAY BY CATEGORY ID, SEASON AND NUMBER*/
    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberHappyPath() {
        whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceIsOk();
        andDeleteMatchDaysByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberNotFound() {
        whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException();
        andDeleteMatchDaysByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberServerError() {
        whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceThrowsServerException();
        andDeleteMatchDaysByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void givenValidMatchDayDTOForRequest(String categoryId, int season, int matchDayNumber) {
        matchDayDTO = new MatchDayDTO(categoryId, season, matchDayNumber, 12345L, 1234567L);
    }

    private void whenFindAllMatchDaysInServiceReturnedValidMatchDays() {
        expectedMatchDay = List.of(
                new MatchDayDTO("PREMIER", 2025, 10, 12345L, 12345678L),
                new MatchDayDTO("PREMIER", 2025, 10, 12345L, 12345678L)
        );

        when(matchDaysBOServiceMock.findAllMatchDays()).thenReturn(expectedMatchDay);
    }

    private void whenFindAllMatchDaysInServiceThrowsNotFoundException() {
        when(matchDaysBOServiceMock.findAllMatchDays()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllMatchDaysInServiceThrowsServerException() {
        when(matchDaysBOServiceMock.findAllMatchDays())
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateMatchDayInServiceReturnedAValidMatchDay() {
        when(matchDaysBOServiceMock.createMatchDay(any()))
                .thenReturn(new MatchDayId("PREMIER", 2025, 10));
    }

    private void whenCreateMatchDayInServiceThrowsServerException() {
        when(matchDaysBOServiceMock.createMatchDay(any(MatchDayDTO.class)))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindMatchDayByCategoryIdInServiceReturnedValidMatchDays() {
        expectedMatchDay = List.of(new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L));

        when(matchDaysBOServiceMock.findMatchDayByCategoryId("PREMIER")).thenReturn(expectedMatchDay);
    }

    private void whenFindMatchDaysByCategoryIdInServiceThrowsNotFoundException() {
        when(matchDaysBOServiceMock.findMatchDayByCategoryId("PREMIER")).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindMatchDaysByCategoryIdInServiceThrowsServerException() {
        when(matchDaysBOServiceMock.findMatchDayByCategoryId("PREMIER"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindMatchDayByCategoryIdAndSeasonInServiceReturnedValidMatchDays() {
        expectedMatchDay = List.of(new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L));

        when(matchDaysBOServiceMock.findMatchDayByCategoryIdAndSeason("PREMIER", 2025)).thenReturn(expectedMatchDay);
    }

    private void whenFindMatchDaysByCategoryIdAndSeasonInServiceThrowsNotFoundException() {
        when(matchDaysBOServiceMock.findMatchDayByCategoryIdAndSeason("PREMIER", 2025)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindMatchDaysByCategoryIdAndSeasonInServiceThrowsServerException() {
        when(matchDaysBOServiceMock.findMatchDayByCategoryIdAndSeason("PREMIER", 2025))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenDeleteMatchDaysByCategoryIdInServiceIsOk() {
        doNothing().when(matchDaysBOServiceMock).deleteMatchDaysByCategoryId(anyString());
    }

    private void whenDeleteMatchDaysByCategoryIdInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(matchDaysBOServiceMock)
                .deleteMatchDaysByCategoryId(anyString());
    }

    private void whenDeleteMatchDaysByCategoryIdInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(matchDaysBOServiceMock)
                .deleteMatchDaysByCategoryId(anyString());
    }

    private void whenDeleteMatchDaysByCategoryIdAndSeasonInServiceIsOk() {
        doNothing().when(matchDaysBOServiceMock).deleteMatchDaysByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void whenDeleteMatchDaysByCategoryIdAndSeasonInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(matchDaysBOServiceMock)
                .deleteMatchDaysByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void whenDeleteMatchDaysByCategoryIdAndSeasonInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(matchDaysBOServiceMock)
                .deleteMatchDaysByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceIsOk() {
        doNothing().when(matchDaysBOServiceMock).deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(matchDaysBOServiceMock)
                .deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(matchDaysBOServiceMock)
                .deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void andFindAllMatchDaysIsCalledInController() {
        result = matchDayBOController.findAllMatchDays(this.requestUid);
    }

    private void andFindMatchDaysByCategoryIdIsCalledInController() {
        result = matchDayBOController.findMatchDaysByCategoryId(this.requestUid, "PREMIER");
    }

    private void andFindMatchDaysByCategoryIdAndSeasonIsCalledInController() {
        result = matchDayBOController.findMatchDaysByCategoryIdAndSeason(this.requestUid, "PREMIER", 2025);
    }

    private void andCreateMatchDayIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = matchDayBOController.createMatchDay(this.requestUid, matchDayDTO, bindingResult);
    }

    private void andDeleteMatchDaysByCategoryIdIsCalledInController() {
        result = matchDayBOController.deleteMatchDaysByCategoryId(this.requestUid, "PREMIER");
    }

    private void andDeleteMatchDaysByCategoryIdAndSeasonIsCalledInController() {
        result = matchDayBOController.deleteMatchDaysByCategoryIdAndSeason(this.requestUid, "PREMIER", 2025);
    }

    private void andDeleteMatchDaysByCategoryIdSeasonAndNumberIsCalledInController() {
        result = matchDayBOController.deleteMatchDayByCategoryIdSeasonAndNumber(this.requestUid, "PREMIER", 2025, 33);
    }

    private void thenVerifyFindAllMatchDaysHasBeenCalledInService() {
        verify(matchDaysBOServiceMock, times(1)).findAllMatchDays();
    }

    private void thenVerifyCreateMatchDayHasBeenCalledInService() {
        verify(matchDaysBOServiceMock, times(1))
                .createMatchDay(any(MatchDayDTO.class));
    }

    private void thenVerifyfindMatchDaysByCategoryIdHasBeenCalledInService() {
        verify(matchDaysBOServiceMock, times(1)).findMatchDayByCategoryId(anyString());
    }

    private void thenVerifyfindMatchDaysByCategoryIdAndSeasonHasBeenCalledInService() {
        verify(matchDaysBOServiceMock, times(1)).findMatchDayByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void thenVerifyDeleteMatchDaysByCategoryIdHasBeenCalledInService() {
        verify(matchDaysBOServiceMock, times(1))
                .deleteMatchDaysByCategoryId(anyString());
    }

    private void thenVerifyDeleteMatchDaysByCategoryIdAndSeasonHasBeenCalledInService() {
        verify(matchDaysBOServiceMock, times(1))
                .deleteMatchDaysByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void thenVerifyDeleteMatchDaysByCategoryIdSeasonAndNumberHasBeenCalledInService() {
        verify(matchDaysBOServiceMock, times(1))
                .deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsMatchDayIdIs(MatchDayId expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

    private void thenReceivedResponseBodyAsMatchDayListIs(List<MatchDayDTO> expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }
}

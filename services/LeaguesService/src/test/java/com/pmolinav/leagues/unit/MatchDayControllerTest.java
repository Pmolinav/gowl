package com.pmolinav.leagues.unit;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.leagueslib.model.MatchDay;
import com.pmolinav.leagueslib.model.MatchDayId;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MatchDayControllerTest extends BaseUnitTest {

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
        whenFindMatchDayByCategoryIdInServiceReturnedValidMatchDay();
        andFindMatchDayByCategoryIdIsCalledInController();
        thenVerifyFindByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsMatchDayListIs(expectedMatchDay);
    }

    @Test
    void findMatchDaysCategoryByIdNotFound() {
        whenFindMatchDayByCategoryIdInServiceThrowsNotFoundException();
        andFindMatchDayByCategoryIdIsCalledInController();
        thenVerifyFindByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findMatchDaysByCategoryIdServerError() {
        whenFindMatchDayByCategoryIdInServiceThrowsServerException();
        andFindMatchDayByCategoryIdIsCalledInController();
        thenVerifyFindByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND MATCH DAYS BY CATEGORY ID AND SEASON */
    @Test
    void findMatchDaysByCategoryIdAndSeasonHappyPath() {
        whenFindMatchDayByCategoryIdAndSeasonInServiceReturnedValidMatchDay();
        andFindMatchDayByCategoryIdAndSeasonIsCalledInController();
        thenVerifyFindByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsMatchDayListIs(expectedMatchDay);
    }

    @Test
    void findMatchDaysCategoryByIdAndSeasonNotFound() {
        whenFindMatchDayByCategoryIdAndSeasonInServiceThrowsNotFoundException();
        andFindMatchDayByCategoryIdAndSeasonIsCalledInController();
        thenVerifyFindByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findMatchDaysByCategoryIdAndSeasonServerError() {
        whenFindMatchDayByCategoryIdAndSeasonInServiceThrowsServerException();
        andFindMatchDayByCategoryIdAndSeasonIsCalledInController();
        thenVerifyFindByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE MATCH DAYS BY CATEGORY ID*/
    @Test
    void deleteMatchDaysByCategoryIdHappyPath() {
        whenDeleteMatchDayByCategoryIdInServiceIsOk();
        andDeleteMatchDayByCategoryIdIsCalledInController();
        thenVerifyDeleteMatchDayByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteMatchDaysByCategoryIdNotFound() {
        whenDeleteMatchDayByCategoryIdInServiceThrowsNotFoundException();
        andDeleteMatchDayByCategoryIdIsCalledInController();
        thenVerifyDeleteMatchDayByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteMatchDaysByCategoryIdServerError() {
        whenDeleteMatchDayByCategoryIdInServiceThrowsServerException();
        andDeleteMatchDayByCategoryIdIsCalledInController();
        thenVerifyDeleteMatchDayByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE MATCH DAY BY CATEGORY ID AND SEASON*/
    @Test
    void deleteMatchDaysByCategoryIdAndSeasonHappyPath() {
        whenDeleteMatchDayByCategoryIdAndSeasonInServiceIsOk();
        andDeleteMatchDayByCategoryIdAndSeasonIsCalledInController();
        thenVerifyDeleteMatchDayByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteMatchDaysByCategoryIdAndSeasonNotFound() {
        whenDeleteMatchDayByCategoryIdAndSeasonInServiceThrowsNotFoundException();
        andDeleteMatchDayByCategoryIdAndSeasonIsCalledInController();
        thenVerifyDeleteMatchDayByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteMatchDaysByCategoryIdAndSeasonServerError() {
        whenDeleteMatchDayByCategoryIdAndSeasonInServiceThrowsServerException();
        andDeleteMatchDayByCategoryIdAndSeasonIsCalledInController();
        thenVerifyDeleteMatchDayByCategoryIdAndSeasonHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE MATCH DAY BY CATEGORY ID, SEASON AND NUMBER*/
    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberHappyPath() {
        whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceIsOk();
        andDeleteMatchDayByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteMatchDayByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberNotFound() {
        whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException();
        andDeleteMatchDayByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteMatchDayByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberServerError() {
        whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceThrowsServerException();
        andDeleteMatchDayByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteMatchDayByCategoryIdSeasonAndNumberHasBeenCalledInService();
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

        when(matchDayServiceMock.findAllMatchDays()).thenReturn(expectedMatchDay);
    }

    private void whenFindAllMatchDaysInServiceThrowsNotFoundException() {
        when(matchDayServiceMock.findAllMatchDays()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllMatchDaysInServiceThrowsServerException() {
        when(matchDayServiceMock.findAllMatchDays())
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateMatchDayInServiceReturnedAValidMatchDay() {
        when(matchDayServiceMock.createMatchDay(any()))
                .thenReturn(new MatchDay("PREMIER", 2025,
                        10, 12345L, 12345678L));
    }

    private void whenCreateMatchDayInServiceThrowsServerException() {
        when(matchDayServiceMock.createMatchDay(any(MatchDayDTO.class)))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindMatchDayByCategoryIdInServiceReturnedValidMatchDay() {
        expectedMatchDay = List.of(new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L));

        when(matchDayServiceMock.findByCategoryId("PREMIER")).thenReturn(expectedMatchDay);
    }

    private void whenFindMatchDayByCategoryIdInServiceThrowsNotFoundException() {
        when(matchDayServiceMock.findByCategoryId("PREMIER")).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindMatchDayByCategoryIdInServiceThrowsServerException() {
        when(matchDayServiceMock.findByCategoryId("PREMIER"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindMatchDayByCategoryIdAndSeasonInServiceReturnedValidMatchDay() {
        expectedMatchDay = List.of(new MatchDayDTO("PREMIER", 2025,
                10, 12345L, 12345678L));

        when(matchDayServiceMock.findByCategoryIdAndSeason("PREMIER", 2025)).thenReturn(expectedMatchDay);
    }

    private void whenFindMatchDayByCategoryIdAndSeasonInServiceThrowsNotFoundException() {
        when(matchDayServiceMock.findByCategoryIdAndSeason("PREMIER", 2025)).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindMatchDayByCategoryIdAndSeasonInServiceThrowsServerException() {
        when(matchDayServiceMock.findByCategoryIdAndSeason("PREMIER", 2025))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenDeleteMatchDayByCategoryIdInServiceIsOk() {
        doNothing().when(matchDayServiceMock).deleteMatchDayByCategoryId(anyString());
    }

    private void whenDeleteMatchDayByCategoryIdInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(matchDayServiceMock)
                .deleteMatchDayByCategoryId(anyString());
    }

    private void whenDeleteMatchDayByCategoryIdInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(matchDayServiceMock)
                .deleteMatchDayByCategoryId(anyString());
    }

    private void whenDeleteMatchDayByCategoryIdAndSeasonInServiceIsOk() {
        doNothing().when(matchDayServiceMock).deleteMatchDayByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void whenDeleteMatchDayByCategoryIdAndSeasonInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(matchDayServiceMock)
                .deleteMatchDayByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void whenDeleteMatchDayByCategoryIdAndSeasonInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(matchDayServiceMock)
                .deleteMatchDayByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceIsOk() {
        doNothing().when(matchDayServiceMock).deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(matchDayServiceMock)
                .deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void whenDeleteMatchDayByCategoryIdSeasonAndNumberInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(matchDayServiceMock)
                .deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void andFindAllMatchDaysIsCalledInController() {
        result = matchDayController.findAllMatchDays();
    }

    private void andFindMatchDayByCategoryIdIsCalledInController() {
        result = matchDayController.findMatchDayByCategoryId("PREMIER");
    }

    private void andFindMatchDayByCategoryIdAndSeasonIsCalledInController() {
        result = matchDayController.findMatchDayByCategoryIdAndSeason("PREMIER", 2025);
    }

    private void andCreateMatchDayIsCalledInController() {
        result = matchDayController.createMatchDay(matchDayDTO);
    }

    private void andDeleteMatchDayByCategoryIdIsCalledInController() {
        result = matchDayController.deleteMatchDayByCategoryId("PREMIER");
    }

    private void andDeleteMatchDayByCategoryIdAndSeasonIsCalledInController() {
        result = matchDayController.deleteMatchDayByCategoryIdAndSeason("PREMIER", 2025);
    }

    private void andDeleteMatchDayByCategoryIdSeasonAndNumberIsCalledInController() {
        result = matchDayController.deleteMatchDayByCategoryIdSeasonAndNumber("PREMIER", 2025, 33);
    }

    private void thenVerifyFindAllMatchDaysHasBeenCalledInService() {
        verify(matchDayServiceMock, times(1)).findAllMatchDays();
    }

    private void thenVerifyCreateMatchDayHasBeenCalledInService() {
        verify(matchDayServiceMock, times(1))
                .createMatchDay(any(MatchDayDTO.class));
    }

    private void thenVerifyFindByCategoryIdHasBeenCalledInService() {
        verify(matchDayServiceMock, times(1)).findByCategoryId(anyString());
    }

    private void thenVerifyFindByCategoryIdAndSeasonHasBeenCalledInService() {
        verify(matchDayServiceMock, times(1)).findByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void thenVerifyDeleteMatchDayByCategoryIdHasBeenCalledInService() {
        verify(matchDayServiceMock, times(1))
                .deleteMatchDayByCategoryId(anyString());
    }

    private void thenVerifyDeleteMatchDayByCategoryIdAndSeasonHasBeenCalledInService() {
        verify(matchDayServiceMock, times(1))
                .deleteMatchDayByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void thenVerifyDeleteMatchDayByCategoryIdSeasonAndNumberHasBeenCalledInService() {
        verify(matchDayServiceMock, times(1))
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

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
        whenDeleteMatchDaysByCategoryIdSeasonAndNumberInServiceIsOk();
        andDeleteMatchDaysByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberNotFound() {
        whenDeleteMatchDaysByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException();
        andDeleteMatchDaysByCategoryIdSeasonAndNumberIsCalledInController();
        thenVerifyDeleteMatchDaysByCategoryIdSeasonAndNumberHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteMatchDaysByCategoryIdSeasonAndNumberServerError() {
        whenDeleteMatchDaysByCategoryIdSeasonAndNumberInServiceThrowsServerException();
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

        when(matchDayServiceMock.findAllMatchDays(nullable(Long.class), nullable(Long.class), nullable(Boolean.class)))
                .thenReturn(expectedMatchDay);
    }

    private void whenFindAllMatchDaysInServiceThrowsNotFoundException() {
        when(matchDayServiceMock.findAllMatchDays(nullable(Long.class), nullable(Long.class), nullable(Boolean.class))).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllMatchDaysInServiceThrowsServerException() {
        when(matchDayServiceMock.findAllMatchDays(nullable(Long.class), nullable(Long.class), nullable(Boolean.class)))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateMatchDayInServiceReturnedAValidMatchDay() {
        when(matchDayServiceMock.createMatchDay(any()))
                .thenReturn(new MatchDay("PREMIER", 2025,
                        10, 12345L, 12345678L, false, false));
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

    private void whenDeleteMatchDaysByCategoryIdInServiceIsOk() {
        doNothing().when(matchDayServiceMock).deleteMatchDaysByCategoryId(anyString());
    }

    private void whenDeleteMatchDaysByCategoryIdInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(matchDayServiceMock)
                .deleteMatchDaysByCategoryId(anyString());
    }

    private void whenDeleteMatchDaysByCategoryIdInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(matchDayServiceMock)
                .deleteMatchDaysByCategoryId(anyString());
    }

    private void whenDeleteMatchDaysByCategoryIdAndSeasonInServiceIsOk() {
        doNothing().when(matchDayServiceMock).deleteMatchDaysByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void whenDeleteMatchDaysByCategoryIdAndSeasonInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(matchDayServiceMock)
                .deleteMatchDaysByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void whenDeleteMatchDaysByCategoryIdAndSeasonInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(matchDayServiceMock)
                .deleteMatchDaysByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void whenDeleteMatchDaysByCategoryIdSeasonAndNumberInServiceIsOk() {
        doNothing().when(matchDayServiceMock).deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void whenDeleteMatchDaysByCategoryIdSeasonAndNumberInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(matchDayServiceMock)
                .deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void whenDeleteMatchDaysByCategoryIdSeasonAndNumberInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(matchDayServiceMock)
                .deleteMatchDayByCategoryIdSeasonAndNumber(anyString(), anyInt(), anyInt());
    }

    private void andFindAllMatchDaysIsCalledInController() {
        result = matchDayController.findAllMatchDays(null, null, null);
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

    private void andDeleteMatchDaysByCategoryIdIsCalledInController() {
        result = matchDayController.deleteMatchDaysByCategoryId("PREMIER");
    }

    private void andDeleteMatchDaysByCategoryIdAndSeasonIsCalledInController() {
        result = matchDayController.deleteMatchDaysByCategoryIdAndSeason("PREMIER", 2025);
    }

    private void andDeleteMatchDaysByCategoryIdSeasonAndNumberIsCalledInController() {
        result = matchDayController.deleteMatchDayByCategoryIdSeasonAndNumber("PREMIER", 2025, 33);
    }

    private void thenVerifyFindAllMatchDaysHasBeenCalledInService() {
        verify(matchDayServiceMock, times(1))
                .findAllMatchDays(nullable(Long.class), nullable(Long.class), nullable(Boolean.class));
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

    private void thenVerifyDeleteMatchDaysByCategoryIdHasBeenCalledInService() {
        verify(matchDayServiceMock, times(1))
                .deleteMatchDaysByCategoryId(anyString());
    }

    private void thenVerifyDeleteMatchDaysByCategoryIdAndSeasonHasBeenCalledInService() {
        verify(matchDayServiceMock, times(1))
                .deleteMatchDaysByCategoryIdAndSeason(anyString(), anyInt());
    }

    private void thenVerifyDeleteMatchDaysByCategoryIdSeasonAndNumberHasBeenCalledInService() {
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

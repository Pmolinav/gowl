package com.pmolinav.league.units;

import com.pmolinav.league.exceptions.CustomStatusException;
import com.pmolinav.league.exceptions.NotFoundException;
import com.pmolinav.leagueslib.model.LeagueCategory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LeagueCategoryControllerTest extends BaseUnitTest {

    private List<LeagueCategory> expectedLeagueCategories;
    private ResponseEntity<?> result;

    /* FIND ALL LEAGUE CATEGORIES */
    @Test
    void findAllLeagueCategoriesHappyPath() {
        whenFindAllLeagueCategoriesInServiceReturnedValidLeagueCategories();
        andFindAllLeagueCategoriesIsCalledInController();
        thenVerifyFindAllLeagueCategoriesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedLeagueCategories);
    }

    @Test
    void findAllLeagueCategoriesNotFound() {
        whenFindAllLeagueCategoriesInServiceThrowsNotFoundException();
        andFindAllLeagueCategoriesIsCalledInController();
        thenVerifyFindAllLeagueCategoriesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllLeagueCategoriesServerError() {
        whenFindAllLeagueCategoriesInServiceThrowsServerException();
        andFindAllLeagueCategoriesIsCalledInController();
        thenVerifyFindAllLeagueCategoriesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND LEAGUE CATEGORY BY ID */
    @Test
    void findLeagueCategoryByIdHappyPath() {
        whenFindLeagueCategoryByIdInServiceReturnedValidLeague();
        andFindLeagueCategoryByIdIsCalledInController();
        thenVerifyFindByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseIs(expectedLeagueCategories.getFirst());
    }

    @Test
    void findLeagueCategoryByIdNotFound() {
        whenFindLeagueCategoryByIdInServiceThrowsNotFoundException();
        andFindLeagueCategoryByIdIsCalledInController();
        thenVerifyFindByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeagueCategoryByIdServerError() {
        whenFindLeagueByIdInServiceThrowsServerException();
        andFindLeagueCategoryByIdIsCalledInController();
        thenVerifyFindByCategoryIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void whenFindAllLeagueCategoriesInServiceReturnedValidLeagueCategories() {
        expectedLeagueCategories = List.of(
                new LeagueCategory("CALCIO", "Calcio Serie A", "Italian League",
                        "FOOTBALL", "IT", "localhost",
                        true, 123L, null),
                new LeagueCategory("CALCIO2", "Calcio Serie B", "Italian League 2",
                        "FOOTBALL", "IT", "localhost",
                        true, 1234L, null)
        );

        when(leagueCategoriesServiceMock.findAllLeagueCategories()).thenReturn(expectedLeagueCategories);
    }

    private void whenFindAllLeagueCategoriesInServiceThrowsNotFoundException() {
        when(leagueCategoriesServiceMock.findAllLeagueCategories())
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllLeagueCategoriesInServiceThrowsServerException() {
        when(leagueCategoriesServiceMock.findAllLeagueCategories())
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindLeagueCategoryByIdInServiceReturnedValidLeague() {
        expectedLeagueCategories = List.of(
                new LeagueCategory("CALCIO", "Calcio Serie A", "Italian League",
                        "FOOTBALL", "IT", "localhost",
                        true, 123L, null)
        );

        when(leagueCategoriesServiceMock.findLeagueCategoryById("CALCIO"))
                .thenReturn(expectedLeagueCategories.getFirst());
    }

    private void whenFindLeagueCategoryByIdInServiceThrowsNotFoundException() {
        when(leagueCategoriesServiceMock.findLeagueCategoryById("CALCIO"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeagueByIdInServiceThrowsServerException() {
        when(leagueCategoriesServiceMock.findLeagueCategoryById("CALCIO"))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void andFindAllLeagueCategoriesIsCalledInController() {
        result = leagueCategoriesController.findAllLeagueCategories(this.requestUid);
    }

    private void andFindLeagueCategoryByIdIsCalledInController() {
        result = leagueCategoriesController.findLeagueCategoryById(this.requestUid, "CALCIO");
    }

    private void thenVerifyFindAllLeagueCategoriesHasBeenCalledInService() {
        verify(leagueCategoriesServiceMock, times(1)).findAllLeagueCategories();
    }

    private void thenVerifyFindByCategoryIdHasBeenCalledInService() {
        verify(leagueCategoriesServiceMock, times(1)).findLeagueCategoryById(anyString());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseIs(LeagueCategory expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

    private void thenReceivedResponseListIs(List<LeagueCategory> expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }
}

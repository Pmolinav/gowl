package com.pmolinav.leagues.unit;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.model.LeagueCategory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LeagueCategoryControllerTest extends BaseUnitTest {

    LeagueCategoryDTO leagueCategoryDTO;
    List<LeagueCategory> expectedCategories;
    ResponseEntity<?> result;

    /* FIND ALL CATEGORIES */
    @Test
    void findAllLeagueCategoriesHappyPath() {
        whenFindAllLeagueCategoriesInServiceReturnedValidLeagueCategories();
        andFindAllLeagueCategoriesIsCalledInController();
        thenVerifyFindAllLeagueCategoriesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedCategories));
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

    /* CREATE LEAGUE CATEGORY */
    @Test
    void createLeagueCategoryHappyPath() {
        givenValidLeagueCategoryDTOForRequest("LA_LIGA", "La Liga Española", "FOOTBALL", "ES");
        whenCreateLeagueCategoryInServiceReturnedAValidLeagueCategory();
        andCreateLeagueCategoryIsCalledInController();
        thenVerifyCreateLeagueCategoryHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsStringIs("LA_LIGA");
    }

    @Test
    void createLeagueCategoryServerError() {
        givenValidLeagueCategoryDTOForRequest("LA_LIGA", "La Liga Española", "FOOTBALL", "ES");
        whenCreateLeagueCategoryInServiceThrowsServerException();
        andCreateLeagueCategoryIsCalledInController();
        thenVerifyCreateLeagueCategoryHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND LEAGUE CATEGORY BY ID */
    @Test
    void findLeagueCategoryByIdHappyPath() {
        whenFindLeagueCategoryByIdInServiceReturnedValidLeagueCategory();
        andFindLeagueCategoryByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedCategories.get(0)));
    }

    @Test
    void findLeagueCategoryByIdNotFound() {
        whenFindLeagueCategoryByIdInServiceThrowsNotFoundException();
        andFindLeagueCategoryByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findLeagueCategoryByIdServerError() {
        whenFindLeagueCategoryByIdInServiceThrowsServerException();
        andFindLeagueCategoryByIdIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE LEAGUE CATEGORY */
    @Test
    void deleteLeagueCategoryHappyPath() {
        whenDeleteLeagueCategoryInServiceIsOk();
        andDeleteLeagueCategoryIsCalledInController();
        thenVerifyDeleteLeagueCategoryHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteLeagueCategoryNotFound() {
        whenDeleteLeagueCategoryInServiceThrowsNotFoundException();
        andDeleteLeagueCategoryIsCalledInController();
        thenVerifyDeleteLeagueCategoryHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteLeagueCategoryServerError() {
        whenDeleteLeagueCategoryInServiceThrowsServerException();
        andDeleteLeagueCategoryIsCalledInController();
        thenVerifyDeleteLeagueCategoryHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void givenValidLeagueCategoryDTOForRequest(String categoryId, String name, String sport, String country) {
        leagueCategoryDTO = new LeagueCategoryDTO(categoryId, name, "some description", sport, country, null, true);
    }

    private void whenFindAllLeagueCategoriesInServiceReturnedValidLeagueCategories() {
        expectedCategories = List.of(
                new LeagueCategory("LA_LIGA", "La Liga Española", "Liga 1D España",
                        "FOOTBALL", "ES", null, true, 1L, 1L),
                new LeagueCategory("PREMIER_LEAGUE", "Premier League", "English 1D",
                        "FOOTBALL", "UK", null, true, 1L, 1L)
        );

        when(leagueCategoryServiceMock.findAllLeagueCategories()).thenReturn(expectedCategories);
    }

    private void whenFindAllLeagueCategoriesInServiceThrowsNotFoundException() {
        when(leagueCategoryServiceMock.findAllLeagueCategories()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllLeagueCategoriesInServiceThrowsServerException() {
        when(leagueCategoryServiceMock.findAllLeagueCategories())
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateLeagueCategoryInServiceReturnedAValidLeagueCategory() {
        when(leagueCategoryServiceMock.createLeagueCategory(any()))
                .thenReturn(new LeagueCategory("LA_LIGA", "La Liga Española",
                        "Liga 1D España", "FOOTBALL", "ES", null,
                        true, 1L, 1L));
    }

    private void whenCreateLeagueCategoryInServiceThrowsServerException() {
        when(leagueCategoryServiceMock.createLeagueCategory(any(LeagueCategoryDTO.class)))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindLeagueCategoryByIdInServiceReturnedValidLeagueCategory() {
        expectedCategories = List.of(new LeagueCategory("LA_LIGA", "La Liga Española",
                "Liga 1D España", "FOOTBALL", "ES", null,
                true, 1L, 1L));

        when(leagueCategoryServiceMock.findById("LA_LIGA")).thenReturn(expectedCategories.get(0));
    }

    private void whenFindLeagueCategoryByIdInServiceThrowsNotFoundException() {
        when(leagueCategoryServiceMock.findById("LA_LIGA")).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeagueCategoryByIdInServiceThrowsServerException() {
        when(leagueCategoryServiceMock.findById("LA_LIGA"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenDeleteLeagueCategoryInServiceIsOk() {
        doNothing().when(leagueCategoryServiceMock).deleteLeagueCategory(anyString());
    }

    private void whenDeleteLeagueCategoryInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leagueCategoryServiceMock)
                .deleteLeagueCategory(anyString());
    }

    private void whenDeleteLeagueCategoryInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(leagueCategoryServiceMock)
                .deleteLeagueCategory(anyString());
    }

    private void andFindAllLeagueCategoriesIsCalledInController() {
        result = leagueCategoryController.findAllLeagueCategories();
    }

    private void andFindLeagueCategoryByIdIsCalledInController() {
        result = leagueCategoryController.findLeagueCategoryById("LA_LIGA");
    }

    private void andCreateLeagueCategoryIsCalledInController() {
        result = leagueCategoryController.createLeagueCategory(leagueCategoryDTO);
    }

    private void andDeleteLeagueCategoryIsCalledInController() {
        result = leagueCategoryController.deleteLeagueCategory("LA_LIGA");
    }

    private void thenVerifyFindAllLeagueCategoriesHasBeenCalledInService() {
        verify(leagueCategoryServiceMock, times(1)).findAllLeagueCategories();
    }

    private void thenVerifyCreateLeagueCategoryHasBeenCalledInService() {
        verify(leagueCategoryServiceMock, times(1))
                .createLeagueCategory(any(LeagueCategoryDTO.class));
    }

    private void thenVerifyFindByIdHasBeenCalledInService() {
        verify(leagueCategoryServiceMock, times(1)).findById(anyString());
    }

    private void thenVerifyDeleteLeagueCategoryHasBeenCalledInService() {
        verify(leagueCategoryServiceMock, times(1)).deleteLeagueCategory(anyString());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, String.valueOf(result.getBody()));
    }
}

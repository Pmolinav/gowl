package com.pmolinav.leagues.units;

import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.model.LeagueCategory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LeagueCategoryBOControllerTest extends BaseUnitTest {

    private LeagueCategoryDTO leagueCategoryDTO;
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

    /* CREATE LEAGUE CATEGORY */
    @Test
    void createLeagueCategoryHappyPath() {
        givenValidLeagueCategoryDTOForRequest();
        whenCreateLeagueCategoryInServiceReturnedAValidLeague();
        andCreateLeagueCategoryIsCalledInController();
        thenVerifyCreateLeagueCategoryHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseStringIs("CALCIO");
    }

    @Test
    void createLeagueCategoryBadRequest() {
        givenValidLeagueCategoryDTOForRequest();
        whenCreateLeagueCategoryInServiceReturnedAValidLeague();
        andCreateLeagueCategoryIsCalledInControllerWithBindingResultErrors();
        thenReceivedStatusCodeIs(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createLeagueCategoryServerError() {
        givenValidLeagueCategoryDTOForRequest();
        whenCreateLeagueInServiceThrowsServerException();
        andCreateLeagueCategoryIsCalledInController();
        thenVerifyCreateLeagueCategoryHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE LEAGUE BY ID */
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

    private void givenValidLeagueCategoryDTOForRequest() {
        leagueCategoryDTO = new LeagueCategoryDTO("CALCIO", "Calcio Serie A",
                "Italian League", "FOOTBALL", "IT", "localhost", true);
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

        when(leagueCategoriesBOServiceMock.findAllLeagueCategories()).thenReturn(expectedLeagueCategories);
    }

    private void whenFindAllLeagueCategoriesInServiceThrowsNotFoundException() {
        when(leagueCategoriesBOServiceMock.findAllLeagueCategories())
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllLeagueCategoriesInServiceThrowsServerException() {
        when(leagueCategoriesBOServiceMock.findAllLeagueCategories())
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenCreateLeagueCategoryInServiceReturnedAValidLeague() {
        when(leagueCategoriesBOServiceMock.createLeagueCategory(any())).thenReturn("CALCIO");
    }

    private void whenCreateLeagueInServiceThrowsServerException() {
        when(leagueCategoriesBOServiceMock.createLeagueCategory(any(LeagueCategoryDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindLeagueCategoryByIdInServiceReturnedValidLeague() {
        expectedLeagueCategories = List.of(
                new LeagueCategory("CALCIO", "Calcio Serie A", "Italian League",
                        "FOOTBALL", "IT", "localhost",
                        true, 123L, null)
        );

        when(leagueCategoriesBOServiceMock.findLeagueCategoryById("CALCIO"))
                .thenReturn(expectedLeagueCategories.getFirst());
    }

    private void whenFindLeagueCategoryByIdInServiceThrowsNotFoundException() {
        when(leagueCategoriesBOServiceMock.findLeagueCategoryById("CALCIO"))
                .thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindLeagueByIdInServiceThrowsServerException() {
        when(leagueCategoriesBOServiceMock.findLeagueCategoryById("CALCIO"))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenDeleteLeagueCategoryInServiceIsOk() {
        doNothing().when(leagueCategoriesBOServiceMock).deleteLeagueCategory(anyString());
    }

    private void whenDeleteLeagueCategoryInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(leagueCategoriesBOServiceMock)
                .deleteLeagueCategory(anyString());
    }

    private void whenDeleteLeagueCategoryInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(leagueCategoriesBOServiceMock)
                .deleteLeagueCategory(anyString());
    }

    private void andFindAllLeagueCategoriesIsCalledInController() {
        result = leagueCategoriesBOController.findAllLeagueCategories(this.requestUid);
    }

    private void andFindLeagueCategoryByIdIsCalledInController() {
        result = leagueCategoriesBOController.findLeagueCategoryById(this.requestUid, "CALCIO");
    }

    private void andCreateLeagueCategoryIsCalledInController() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        result = leagueCategoriesBOController.createLeagueCategory(this.requestUid, leagueCategoryDTO, bindingResult);
    }

    private void andCreateLeagueCategoryIsCalledInControllerWithBindingResultErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("userDTO", "username", "Name is mandatory.")
        ));

        result = leagueCategoriesBOController.createLeagueCategory(this.requestUid, leagueCategoryDTO, bindingResult);
    }

    private void andDeleteLeagueCategoryIsCalledInController() {
        result = leagueCategoriesBOController.deleteLeagueCategory(this.requestUid, "CALCIO");
    }

    private void thenVerifyFindAllLeagueCategoriesHasBeenCalledInService() {
        verify(leagueCategoriesBOServiceMock, times(1)).findAllLeagueCategories();
    }

    private void thenVerifyFindByCategoryIdHasBeenCalledInService() {
        verify(leagueCategoriesBOServiceMock, times(1)).findLeagueCategoryById(anyString());
    }

    private void thenVerifyCreateLeagueCategoryHasBeenCalledInService() {
        verify(leagueCategoriesBOServiceMock, times(1)).createLeagueCategory(any(LeagueCategoryDTO.class));
    }

    private void thenVerifyDeleteLeagueCategoryHasBeenCalledInService() {
        verify(leagueCategoriesBOServiceMock, times(1)).deleteLeagueCategory(anyString());
    }


    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseIs(LeagueCategory expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

    private void thenReceivedResponseStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }

    private void thenReceivedResponseListIs(List<LeagueCategory> expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, result.getBody());
    }
}

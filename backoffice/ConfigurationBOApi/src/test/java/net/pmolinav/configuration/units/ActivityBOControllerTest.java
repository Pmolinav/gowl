package net.pmolinav.configuration.units;

import net.pmolinav.bookingslib.dto.ActivityDTO;
import net.pmolinav.bookingslib.exception.CustomStatusException;
import net.pmolinav.bookingslib.exception.InternalServerErrorException;
import net.pmolinav.bookingslib.exception.NotFoundException;
import net.pmolinav.bookingslib.model.Activity;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ActivityBOControllerTest extends BaseUnitTest {

    ActivityDTO activityDTO;
    List<Activity> expectedActivities;
    ResponseEntity<?> result;

    /* FIND ALL ACTIVITIES */
    @Test
    void findAllActivitiesHappyPath() {
        whenFindAllActivitiesInServiceReturnedValidActivities();
        andFindAllActivitiesIsCalledInController();
        thenVerifyFindAllActivitiesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedActivities));
    }

    @Test
    void findAllActivitiesNotFound() {
        whenFindAllActivitiesInServiceThrowsNotFoundException();
        assertThrows(NotFoundException.class, this::andFindAllActivitiesIsCalledInController);
        thenVerifyFindAllActivitiesHasBeenCalledInService();
    }

    @Test
    void findAllActivitiesServerError() {
        whenFindAllActivitiesInServiceThrowsServerException();
        assertThrows(InternalServerErrorException.class, this::andFindAllActivitiesIsCalledInController);
        thenVerifyFindAllActivitiesHasBeenCalledInService();
    }

    /* CREATE ACTIVITY */
    @Test
    void createActivityHappyPath() {
        givenValidActivityDTOForRequest("someActivity", "someDescription", 100);
        whenCreateActivityInServiceReturnedAValidActivity();
        andCreateActivityIsCalledInController();
        thenVerifyCreateActivityHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsStringIs(activityDTO.getActivityName());
    }

    @Test
    void createActivityServerError() {
        givenValidActivityDTOForRequest("someActivity", "someDescription", 100);
        whenCreateActivityInServiceThrowsServerException();
        andCreateActivityIsCalledInController();
        thenVerifyCreateActivityHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND ACTIVITY BY ID */
    @Test
    void findActivityByIdHappyPath() {
        whenFindActivityByNameInServiceReturnedValidActivities();
        andFindActivityByIdIsCalledInController();
        thenVerifyFindByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedActivities.get(0)));
    }

    @Test
    void findActivityByIdNotFound() {
        whenFindActivityByNameInServiceThrowsNotFoundException();
        andFindActivityByIdIsCalledInController();
        thenVerifyFindByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findActivityByIdServerError() {
        whenFindActivityByNameInServiceThrowsServerException();
        andFindActivityByIdIsCalledInController();
        thenVerifyFindByNameHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE ACTIVITY */
    @Test
    void deleteActivityHappyPath() {
        whenDeleteActivityInServiceIsOk();
        andDeleteActivityIsCalledInController();
        thenVerifyDeleteActivityHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteActivityNotFound() {
        whenDeleteActivityInServiceThrowsNotFoundException();
        andDeleteActivityIsCalledInController();
        thenVerifyDeleteActivityHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteActivityServerError() {
        whenDeleteActivityInServiceThrowsServerException();
        andDeleteActivityIsCalledInController();
        thenVerifyDeleteActivityHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void givenValidActivityDTOForRequest(String name, String description, Integer price) {
        activityDTO = new ActivityDTO(name, description, price);
    }

    private void whenFindAllActivitiesInServiceReturnedValidActivities() {
        expectedActivities = List.of(
                new Activity("someActivity", "someDescription",
                        100, new Date(), null),
                new Activity("otherActivity", "otherDescription",
                        10, new Date(), null));

        when(activityBOServiceMock.findAllActivities()).thenReturn(expectedActivities);
    }

    private void whenFindAllActivitiesInServiceThrowsNotFoundException() {
        when(activityBOServiceMock.findAllActivities()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllActivitiesInServiceThrowsServerException() {
        when(activityBOServiceMock.findAllActivities())
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateActivityInServiceReturnedAValidActivity() {
        when(activityBOServiceMock.createActivity(any())).thenReturn("someActivity");
    }

    private void whenCreateActivityInServiceThrowsServerException() {
        when(activityBOServiceMock.createActivity(any(ActivityDTO.class)))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenFindActivityByNameInServiceReturnedValidActivities() {
        expectedActivities = List.of(
                new Activity("someActivity", "someDescription",
                        100, new Date(), null));

        when(activityBOServiceMock.findActivityByName("someActivity")).thenReturn(expectedActivities.get(0));
    }

    private void whenFindActivityByNameInServiceThrowsNotFoundException() {
        when(activityBOServiceMock.findActivityByName("someActivity")).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindActivityByNameInServiceThrowsServerException() {
        when(activityBOServiceMock.findActivityByName("someActivity"))
                .thenThrow(new CustomStatusException("Internal Server Error", 500));
    }

    private void whenDeleteActivityInServiceIsOk() {
        doNothing().when(activityBOServiceMock).deleteActivity(anyString());
    }

    private void whenDeleteActivityInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(activityBOServiceMock)
                .deleteActivity(anyString());
    }

    private void whenDeleteActivityInServiceThrowsServerException() {
        doThrow(new CustomStatusException("Internal Server Error", 500))
                .when(activityBOServiceMock)
                .deleteActivity(anyString());
    }

    private void andFindAllActivitiesIsCalledInController() {
        result = activityBOController.findAllActivities(this.requestUid);
    }

    private void andFindActivityByIdIsCalledInController() {
        result = activityBOController.getActivityByName(this.requestUid, "someActivity");
    }

    private void andCreateActivityIsCalledInController() {
        result = activityBOController.createActivity(this.requestUid, activityDTO);
    }

    private void andDeleteActivityIsCalledInController() {
        result = activityBOController.deleteActivity(this.requestUid, "someActivity");
    }

    private void thenVerifyFindAllActivitiesHasBeenCalledInService() {
        verify(activityBOServiceMock, times(1)).findAllActivities();
    }

    private void thenVerifyCreateActivityHasBeenCalledInService() {
        verify(activityBOServiceMock, times(1)).createActivity(any(ActivityDTO.class));
    }

    private void thenVerifyFindByNameHasBeenCalledInService() {
        verify(activityBOServiceMock, times(1)).findActivityByName(anyString());
    }

    private void thenVerifyDeleteActivityHasBeenCalledInService() {
        verify(activityBOServiceMock, times(1)).deleteActivity(anyString());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, String.valueOf(result.getBody()));
    }
}

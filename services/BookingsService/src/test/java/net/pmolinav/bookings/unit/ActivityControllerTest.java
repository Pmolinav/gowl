package net.pmolinav.bookings.unit;

import net.pmolinav.bookingslib.dto.ActivityDTO;
import net.pmolinav.bookingslib.exception.InternalServerErrorException;
import net.pmolinav.bookingslib.exception.NotFoundException;
import net.pmolinav.bookingslib.model.Activity;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ActivityControllerTest extends BaseUnitTest {

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
        andFindAllActivitiesIsCalledInController();
        thenVerifyFindAllActivitiesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllActivitiesServerError() {
        whenFindAllActivitiesInServiceThrowsServerException();
        andFindAllActivitiesIsCalledInController();
        thenVerifyFindAllActivitiesHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CREATE ACTIVITY */
    @Test
    void createActivityHappyPath() {
        givenValidActivityDTOForRequest("someActivity", "someDescription", 100);
        whenCreateActivityInServiceReturnedAValidActivity();
        andCreateActivityIsCalledInController();
        thenVerifyCreateActivityHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseBodyAsStringIs("someActivity");
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
    void findActivityByNameHappyPath() {
        whenFindActivityByNameInServiceReturnedValidActivities();
        andFindActivityByNameIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseBodyAsStringIs(String.valueOf(expectedActivities.get(0)));
    }

    @Test
    void findActivityByNameNotFound() {
        whenFindActivityByNameInServiceThrowsNotFoundException();
        andFindActivityByNameIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findActivityByNameServerError() {
        whenFindActivityByNameInServiceThrowsServerException();
        andFindActivityByNameIsCalledInController();
        thenVerifyFindByIdHasBeenCalledInService();
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

        when(activityServiceMock.findAllActivities()).thenReturn(expectedActivities);
    }

    private void whenFindAllActivitiesInServiceThrowsNotFoundException() {
        when(activityServiceMock.findAllActivities()).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindAllActivitiesInServiceThrowsServerException() {
        when(activityServiceMock.findAllActivities())
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenCreateActivityInServiceReturnedAValidActivity() {
        when(activityServiceMock.createActivity(any())).thenReturn(new Activity(
                activityDTO.getActivityName(), activityDTO.getDescription(), 100, new Date(), null));
    }

    private void whenCreateActivityInServiceThrowsServerException() {
        when(activityServiceMock.createActivity(any(ActivityDTO.class)))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenFindActivityByNameInServiceReturnedValidActivities() {
        expectedActivities = List.of(
                new Activity("someActivity", "someDescription",
                        100, new Date(), null));

        when(activityServiceMock.findByName("someActivity")).thenReturn(expectedActivities.get(0));
    }

    private void whenFindActivityByNameInServiceThrowsNotFoundException() {
        when(activityServiceMock.findByName("someActivity")).thenThrow(new NotFoundException("Not Found"));
    }

    private void whenFindActivityByNameInServiceThrowsServerException() {
        when(activityServiceMock.findByName("someActivity"))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));
    }

    private void whenDeleteActivityInServiceIsOk() {
        doNothing().when(activityServiceMock).deleteActivity(anyString());
    }

    private void whenDeleteActivityInServiceThrowsNotFoundException() {
        doThrow(new NotFoundException("Not Found"))
                .when(activityServiceMock)
                .deleteActivity(anyString());
    }

    private void whenDeleteActivityInServiceThrowsServerException() {
        doThrow(new InternalServerErrorException("Internal Server Error"))
                .when(activityServiceMock)
                .deleteActivity(anyString());
    }

    private void andFindAllActivitiesIsCalledInController() {
        result = activityController.findAllActivities();
    }

    private void andFindActivityByNameIsCalledInController() {
        result = activityController.findActivityByName("someActivity");
    }

    private void andCreateActivityIsCalledInController() {
        result = activityController.createActivity(activityDTO);
    }

    private void andDeleteActivityIsCalledInController() {
        result = activityController.deleteActivity("someActivity");
    }

    private void thenVerifyFindAllActivitiesHasBeenCalledInService() {
        verify(activityServiceMock, times(1)).findAllActivities();
    }

    private void thenVerifyCreateActivityHasBeenCalledInService() {
        verify(activityServiceMock, times(1)).createActivity(any(ActivityDTO.class));
    }

    private void thenVerifyFindByIdHasBeenCalledInService() {
        verify(activityServiceMock, times(1)).findByName(anyString());
    }

    private void thenVerifyDeleteActivityHasBeenCalledInService() {
        verify(activityServiceMock, times(1)).deleteActivity(anyString());
    }

    private void thenReceivedStatusCodeIs(HttpStatus httpStatus) {
        assertEquals(httpStatus, result.getStatusCode());
    }

    private void thenReceivedResponseBodyAsStringIs(String expectedResult) {
        assertNotNull(result);
        assertEquals(expectedResult, String.valueOf(result.getBody()));
    }
}

package net.pmolinav.configuration.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pmolinav.bookingslib.dto.ActivityDTO;
import net.pmolinav.bookingslib.model.Activity;
import net.pmolinav.configuration.client.ActivityClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("net.pmolinav.bookingslib.model")
class ActivityBOControllerIntegrationTest extends AbstractBaseTest {

    @MockBean
    private ActivityClient activityClient;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Activity> expectedActivities;

    @Test
    void findAllActivitiesInternalServerError() throws Exception {
        andFindAllActivitiesThrowsNonRetryableException();

        mockMvc.perform(get("/activities?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findAllActivitiesHappyPath() throws Exception {
        andFindAllActivitiesReturnedValidActivities();

        MvcResult result = mockMvc.perform(get("/activities?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        List<Activity> activityResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Activity>>() {
                });

        Assertions.assertEquals(expectedActivities, activityResponseList);
    }

    @Test
    void createActivityServerError() throws Exception {
        andCreateActivityThrowsNonRetryableException();

        ActivityDTO requestDto = new ActivityDTO("Gym", "Gym activity", 25);

        mockMvc.perform(post("/activities?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createActivityHappyPath() throws Exception {
        andCreateActivityReturnedValidName();

        ActivityDTO requestDto = new ActivityDTO("Gym", "Gym activity", 25);

        MvcResult result = mockMvc.perform(post("/activities?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody, matchesPattern("\\w+"));
    }

    @Test
    void findActivityByIdServerError() throws Exception {
        andFindActivityByNameThrowsNonRetryableException();

        mockMvc.perform(get("/activities/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void findActivityByIdHappyPath() throws Exception {
        andFindActivityByNameReturnedActivity();

        MvcResult result = mockMvc.perform(get("/activities/3?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk())
                .andReturn();

        Activity activityResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Activity>() {
                });

        Assertions.assertEquals(expectedActivities.get(0), activityResponse);
    }

    @Test
    void deleteActivityByIdInternalServerError() throws Exception {
        andActivityDeleteThrowsNonRetryableException();

        mockMvc.perform(delete("/activities/123?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteActivityByIdHappyPath() throws Exception {
        andActivityIsDeletedOkOnClient();

        mockMvc.perform(delete("/activities/5?requestUid=" + requestUid)
                        .header(HttpHeaders.AUTHORIZATION, authToken))
                .andExpect(status().isOk());
    }

    private void andActivityIsDeletedOkOnClient() {
        doNothing().when(this.activityClient).deleteActivity(anyString());
    }

    private void andActivityDeleteThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.activityClient).deleteActivity(anyString());
    }

    private void andFindActivityByNameReturnedActivity() {
        this.expectedActivities = List.of(new Activity("someActivity",
                "someDescription", 100, new Date(), null));

        when(this.activityClient.findActivityByName(anyString())).thenReturn(this.expectedActivities.get(0));
    }

    private void andFindActivityByNameThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.activityClient).findActivityByName(anyString());
    }

    private void andCreateActivityReturnedValidName() {
        when(this.activityClient.createActivity(any(ActivityDTO.class))).thenReturn("someActivity");
    }

    private void andCreateActivityThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.activityClient).createActivity(any(ActivityDTO.class));
    }

    private void andFindAllActivitiesReturnedValidActivities() {
        this.expectedActivities = List.of(new Activity("someActivity",
                "someDescription", 100, new Date(), null));

        when(this.activityClient.findAllActivities()).thenReturn(this.expectedActivities);
    }

    private void andFindAllActivitiesThrowsNonRetryableException() {
        doThrow(new RuntimeException("someException")).when(this.activityClient).findAllActivities();
    }
}


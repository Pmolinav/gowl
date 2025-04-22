package com.pmolinav.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.pmolinav.bookingslib.dto.ActivityDTO;
import com.pmolinav.bookingslib.model.Activity;
import com.pmolinav.database.GowlDatabaseConnector;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ActivityBOConfigurationDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8002";

    @When("^try to create a new activity with data$")
    public void tryToCreateANewActivity(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            for (Map<String, String> row : rows) {
                executePost(localURL + "/activities",
                        objectMapper.writeValueAsString(new ActivityDTO(
                                row.get("name"),
                                row.get("description"),
                                Integer.parseInt(row.get("price"))
                        )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get all activities")
    public void tryToGetAllActivities() {
        executeGet(localURL + "/activities");
    }

    @When("^try to get an activity by activity name$")
    public void tryToGetAnActivityByActivityName() {
        executeGet(localURL + "/activities/" + lastActivity.getActivityName());
    }

    @When("^try to delete an activity by activity name$")
    public void tryToDeleteAnActivityByActivityName() {
        executeDelete(localURL + "/activities/" + lastActivity.getActivityName());
    }

    @Then("an activity with name (.*) has been stored successfully$")
    public void anActivityHasBeenStored(String name) {
        try {
            dbConnector = new GowlDatabaseConnector();
            lastActivity = dbConnector.getActivityByName(name);
            assertNotNull(lastActivity);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("a list of activities with names (.*) are returned in response$")
    public void aListOfActivitiesWithNamesIsReturned(String names) throws JsonProcessingException {
        List<String> namesList = List.of(names.split(","));
        List<Activity> obtainedActivities = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<Activity>>() {
        });
        assertNotNull(obtainedActivities);
        for (String name : namesList) {
            assertTrue(obtainedActivities.stream().anyMatch(activity -> activity.getActivityName().equals(name)));
        }
    }

    @Then("an activity with name (.*) is returned in response$")
    public void anActivityWithNameIsReturned(String name) throws JsonProcessingException {
        Activity obtainedActivity = objectMapper.readValue(latestResponse.getBody(), Activity.class);
        assertNotNull(obtainedActivity);
        assertEquals(name, obtainedActivity.getActivityName());
    }

}
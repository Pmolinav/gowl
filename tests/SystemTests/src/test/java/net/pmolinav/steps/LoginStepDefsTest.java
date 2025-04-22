package com.pmolinav.steps;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.pmolinav.database.GowlDatabaseConnector;
import com.pmolinav.dto.AuthCredentials;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LoginStepDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8002";

    @Before
    public static void cleanAllAfterTests() {
        try {
            dbConnector = new GowlDatabaseConnector();
            dbConnector.deleteUsers();
            dbConnector.deleteActivities();
            dbConnector.deleteBookings();
            dbConnector.deleteHistory();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("^invalid auth token$")
    public void givenInvalidAuthToken() {
        authToken = "invalidAuthToken";
    }

    @When("^try to get health")
    public void tryToGetHealth() {
        executeGet(localURL + "/health");
    }

    @When("^an user with username (.*) and password (.*) tries to log in$")
    public void anUserTriesToLogIn(String user, String password) {
        try {
            executePost(localURL + "/login",
                    objectMapper.writeValueAsString(new AuthCredentials(user, password)));
            authToken = authResponse.getHeader(HttpHeaders.AUTHORIZATION);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("^received status code is (\\d+)$")
    public void receivedStatusCodeIs(int expectedStatusCode) throws IOException {
        HttpStatus currentStatusCode = latestResponse.getResponse().getStatusCode();
        assertEquals(expectedStatusCode, currentStatusCode.value());
    }
}
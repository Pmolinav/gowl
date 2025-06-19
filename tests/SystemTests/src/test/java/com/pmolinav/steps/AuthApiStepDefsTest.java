package com.pmolinav.steps;

import com.pmolinav.database.LeaguesDatabaseConnector;
import com.pmolinav.database.UsersDatabaseConnector;
import com.pmolinav.userslib.dto.UserDTO;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AuthApiStepDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8003";

    @BeforeAll
    public static void connectToDatabases() {
        try {
            usersDbConnector = new UsersDatabaseConnector();
            leaguesDbConnector = new LeaguesDatabaseConnector();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Before
    public static void cleanAllBeforeTests() {
        try {
            usersDbConnector.deleteAll();
            leaguesDbConnector.deleteAll();
            predictionsDbConnector.deleteAll();
//            dbConnector.deleteHistory();
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("^wait for (\\d+) seconds$")
    public void givenInvalidAuthToken(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
    }

    @Given("^invalid auth token$")
    public void givenInvalidAuthToken() {
        authToken = "invalidAuthToken";
    }

    @When("^try to get AuthApi health")
    public void tryToGetHealth() {
        executeGet(localURL + "/health");
    }

    @When("^an user with username (.*) and password (.*) tries to log in$")
    public void anUserTriesToLogIn(String user, String password) {
        try {
            UserDTO userRequest = new UserDTO();
            userRequest.setUsername(user);
            userRequest.setPassword(password);

            executePost(localURL + "/login",
                    objectMapper.writeValueAsString(userRequest));

            authToken = authResponse.getHeader(HttpHeaders.AUTHORIZATION);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("^received status code is (\\d+)$")
    public void receivedStatusCodeIs(int expectedStatusCode) throws IOException {
        HttpStatusCode currentStatusCode = latestResponse.getResponse().getStatusCode();
        assertEquals(expectedStatusCode, currentStatusCode.value());
    }
}
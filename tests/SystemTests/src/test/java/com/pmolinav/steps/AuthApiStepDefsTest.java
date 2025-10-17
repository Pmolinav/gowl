package com.pmolinav.steps;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.pmolinav.database.LeaguesDatabaseConnector;
import com.pmolinav.database.PredictionsDatabaseConnector;
import com.pmolinav.database.UsersDatabaseConnector;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.dto.UserPublicDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AuthApiStepDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8003";

    @BeforeAll
    public static void connectToDatabases() {
        try {
            usersDbConnector = new UsersDatabaseConnector();
            leaguesDbConnector = new LeaguesDatabaseConnector();
            predictionsDbConnector = new PredictionsDatabaseConnector();

            wireMock = new WireMock(WIREMOCK_HOST, WIREMOCK_PORT);
            wireMock.resetMappings();
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

    @When("^try to create a new user with data with public endpoint$")
    public void tryToCreateANewUserPublicEndpoint(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            for (Map<String, String> row : rows) {
                String[] date = row.get("birthDate").split("-");
                executePost(localURL + "/users",
                        objectMapper.writeValueAsString(new UserPublicDTO(row.get("username"),
                                row.get("password"),
                                row.get("name"),
                                row.get("email"),
                                LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]))
                        )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get an user by userId with public endpoint$")
    public void tryToGetAnUserByUserIdPublicEndpoint() {
        executeGet(localURL + "/users/" + lastUser.getUserId());
    }

    @When("^try to get an user by username with public endpoint$")
    public void tryToGetAnUserByLastUsernamePublicEndpoint() {
        executeGet(localURL + "/users/username/" + lastUser.getUsername());
    }

    @When("^try to get an user by username (\\w+) with public endpoint$")
    public void tryToGetAnUserByUsernamePublicEndpoint(String username) {
        executeGet(localURL + "/users/username/" + username);
    }

    @When("^try to delete an user by userId with public endpoint$")
    public void tryToDeleteAnUserByUserIdPublicEndpoint() {
        executeDelete(localURL + "/users/" + lastUser.getUserId());
    }


    @Then("^received status code is (\\d+)$")
    public void receivedStatusCodeIs(int expectedStatusCode) throws IOException {
        HttpStatusCode currentStatusCode = latestResponse.getResponse().getStatusCode();
        assertEquals(expectedStatusCode, currentStatusCode.value());
    }
}
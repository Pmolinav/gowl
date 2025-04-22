package com.pmolinav.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.pmolinav.bookingslib.dto.Role;
import com.pmolinav.bookingslib.dto.UserDTO;
import com.pmolinav.bookingslib.model.User;
import com.pmolinav.database.GowlDatabaseConnector;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class UserBOConfigurationDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8002";

    @Given("the following users have been stored previously$")
    public void storeUsersInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            // Insert each requested player
            for (Map<String, String> row : rows) {
                dbConnector.insertUser(new User(null,
                                row.get("username"),
                                new BCryptPasswordEncoder().encode(row.get("password")),
                                row.get("name"),
                                row.get("email"),
                                row.get("role"),
                                row.get("creation_date") != null ?
                                        new Date(Long.parseLong(row.get("creation_date"))) : new Date(),
                                row.get("modification_date") != null ?
                                        new Date(Long.parseLong(row.get("modification_date"))) : new Date()
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to create a new user with data$")
    public void tryToCreateANewUser(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            for (Map<String, String> row : rows) {
                executePost(localURL + "/users",
                        objectMapper.writeValueAsString(new UserDTO(row.get("username"),
                                row.get("password"),
                                row.get("name"),
                                row.get("email"),
                                Role.valueOf(row.get("role"))
                        )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get all users$")
    public void tryToGetAllUsers() {
        executeGet(localURL + "/users");
    }

    @When("^try to get an user by userId$")
    public void tryToGetAnUserByUserId() {
        executeGet(localURL + "/users/" + lastUser.getUserId());
    }

    @When("^try to delete an user by userId$")
    public void tryToDeleteAnUserByUserId() {
        executeDelete(localURL + "/users/" + lastUser.getUserId());
    }

    @Then("an user with username (.*) has been stored successfully$")
    public void anUserHasBeenStored(String username) {
        try {
            dbConnector = new GowlDatabaseConnector();
            lastUser = dbConnector.getUserByUsername(username);
            assertNotNull(lastUser);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("a list of users with usernames (.*) are returned in response$")
    public void aListOfUsersWithUsernamesIsReturned(String usernames) throws JsonProcessingException {
        List<String> usernamesList = List.of(usernames.split(","));
        List<User> obtainedUsers = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<User>>() {
        });
        assertNotNull(obtainedUsers);
        for (String username : usernamesList) {
            assertTrue(obtainedUsers.stream().anyMatch(user -> user.getUsername().equals(username)));
        }
    }

    @Then("an user with username (.*) is returned in response$")
    public void anUserWithUsernameIsReturned(String username) throws JsonProcessingException {
        User obtainedUser = objectMapper.readValue(latestResponse.getBody(), User.class);
        assertNotNull(obtainedUser);
        assertEquals(username, obtainedUser.getUsername());
    }

}
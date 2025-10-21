package com.pmolinav.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.Token;
import com.pmolinav.userslib.model.User;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class UsersBOApiDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8002";

    @Given("the following roles have been stored previously$")
    public void storeRolesInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            List<String> roles = rows.stream()
                    .map(row -> row.get("role"))
                    .collect(Collectors.toList());

            // Insert each requested role
            usersDbConnector.insertRoles(roles);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("the following users have been stored previously$")
    public void storeUsersInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            // Insert each requested player
            for (Map<String, String> row : rows) {
                String[] date = row.get("birthDate").split("-");
                usersDbConnector.insertUser(new User(null,
                                row.get("username"),
                                new BCryptPasswordEncoder().encode(row.get("password")),
                                row.get("name"),
                                row.get("email"),
                                LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0])),
                                row.get("creation_date") != null ?
                                        Long.parseLong(row.get("creation_date")) : new Date().getTime(),
                                row.get("modification_date") != null ?
                                        Long.parseLong(row.get("modification_date")) : new Date().getTime(),
                                usersDbConnector.getRolesByNames(List.of(row.get("roles").split(",")))
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
                String[] date = row.get("birthDate").split("-");
                executePost(localURL + "/users",
                        objectMapper.writeValueAsString(new UserDTO(row.get("username"),
                                row.get("password"),
                                row.get("name"),
                                row.get("email"),
                                LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0])),
                                Boolean.parseBoolean(row.get("admin")))
                        ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get UsersBOApi health")
    public void tryToGetHealth() {
        executeGet(localURL + "/health");
    }

    @When("^try to get all users$")
    public void tryToGetAllUsers() {
        executeGet(localURL + "/users");
    }

    @When("^try to get an user by userId$")
    public void tryToGetAnUserByUserId() {
        executeGet(localURL + "/users/" + lastUser.getUserId());
    }

    @When("^try to get an user by username$")
    public void tryToGetAnUserByUsername() {
        executeGet(localURL + "/users/username/" + lastUser.getUsername());
    }

    @When("^try to delete an user by userId$")
    public void tryToDeleteAnUserByUserId() {
        executeDelete(localURL + "/users/" + lastUser.getUserId());
    }

    @Then("an user with username (.*) has been stored successfully$")
    public void anUserHasBeenStored(String username) {
        try {
            lastUser = usersDbConnector.getUserByUsername(username);
            assertNotNull(lastUser);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("refresh token has been stored successfully for user (.*)$")
    public void refreshTokenHasBeenStoredForUser(String username) {
        try {
            List<Token> tokens = usersDbConnector.getTokensByUsername(username);
            assertFalse(CollectionUtils.isEmpty(tokens));
            assertTrue(tokens.stream().anyMatch(token -> token.getRefreshToken().equals(refreshToken)));
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("refresh token has been removed for user (.*)$")
    public void refreshTokenHasBeenRemovedForUser(String username) {
        try {
            List<Token> tokens = usersDbConnector.getTokensByUsername(username);
            assertFalse(CollectionUtils.isEmpty(tokens));
            assertFalse(tokens.stream().anyMatch(token -> token.getRefreshToken().equals(refreshToken)));
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("all tokens has been removed for user (.*)$")
    public void allTokensHaveBeenRemovedForUser(String username) {
        try {
            List<Token> tokens = usersDbConnector.getTokensByUsername(username);
            assertTrue(CollectionUtils.isEmpty(tokens));
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
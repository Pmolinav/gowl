Feature: UserBOConfiguration

  Background:
    Given the following roles have been stored previously
      | role         |
      | ROLE_USER    |
      | ROLE_PREMIUM |
      | ROLE_ADMIN   |
    Given the following users have been stored previously
      | username | password     | name     | email          | roles      | creation_date | modification_date |
      | someUser | somePassword | someName | some@email.com | ROLE_ADMIN | 123456        | 123456            |
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200

  Scenario: Create a new user unauthorized
    Given invalid auth token
    When try to create a new user with data
      | username | password    | name     | email         | isAdmin |
      | newUser  | newPassword | someName | new@email.com | true    |
    Then received status code is 403

  Scenario: Create a new user successfully
    When try to create a new user with data
      | username | password    | name     | email         | isAdmin |
      | newUser  | newPassword | someName | new@email.com | true    |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
#    Then an entity User with id N/A has been stored into historical by user Admin and with type CREATE

  Scenario: Create a new user bad request
    When try to create a new user with data
      | username | password    | email         | isAdmin |
      | newUser  | newPassword | new@email.com | true    |
    Then received status code is 400

  Scenario: Get all users successfully
    When try to create a new user with data
      | username  | password      | name      | email           | isAdmin |
      | newUser   | newPassword   | someName  | new@email.com   | true    |
      | otherUser | otherPassword | otherName | other@email.com | true    |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    Then an user with username otherUser has been stored successfully
    When try to get all users
    Then received status code is 200
    Then a list of users with usernames someUser,newUser,otherUser are returned in response

  Scenario: Get user by userId successfully
    When try to create a new user with data
      | username | password    | name     | email          | isAdmin |
      | newUser  | newPassword | someName | some@email.com | false   |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When try to get an user by userId
    Then received status code is 200
    Then an user with username newUser is returned in response

  Scenario: Delete user by userId successfully
    When try to create a new user with data
      | username | password    | name     | email          | isAdmin |
      | newUser  | newPassword | someName | some@email.com | true    |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When try to delete an user by userId
    Then received status code is 200
#    Then an entity User with id N/A has been stored into historical by user Admin and with type CREATE
#    Then an entity User with id N/A has been stored into historical by user Admin and with type DELETE
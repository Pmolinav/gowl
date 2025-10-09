Feature: UserBOApi

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

  Scenario: Create a new user with no auth
    Given invalid auth token
    When try to create a new user with data
      | username | password    | name    | email         | is_admin |
      | newUser  | newPassword | newName | new@email.com | true     |
    Then received status code is 201

  Scenario: Create a new user successfully
    When try to create a new user with data
      | username | password    | name    | email         | is_admin |
      | newUser  | newPassword | newName | new@email.com | true     |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
#    Then an entity User with id N/A has been stored into historical by user Admin and with type CREATE

  Scenario: Create a new user bad request
    When try to create a new user with data
      | username | password    | email         | is_admin |
      | newUser  | newPassword | new@email.com | true     |
    Then received status code is 400

  Scenario: Get all users successfully
    When try to create a new user with data
      | username  | password      | name      | email           | is_admin |
      | newUser   | newPassword   | newName   | new@email.com   | true     |
      | otherUser | otherPassword | otherName | other@email.com | true     |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    Then an user with username otherUser has been stored successfully
    When try to get all users
    Then received status code is 200
    Then a list of users with usernames someUser,newUser,otherUser are returned in response

  Scenario: Get user by userId successfully
    When try to create a new user with data
      | username | password    | name    | email          | is_admin |
      | newUser  | newPassword | newName | some@email.com | false    |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When try to get an user by userId
    Then received status code is 200
    Then an user with username newUser is returned in response

  Scenario: A normal user is not authorized to get user by userId
    When try to create a new user with data
      | username | password    | name    | email          | is_admin |
      | newUser  | newPassword | newName | some@email.com | false    |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When an user with username newUser and password newPassword tries to log in
    Then received status code is 200
    When try to get an user by userId
    Then received status code is 403

  Scenario: Get user by username successfully
    When try to create a new user with data
      | username | password    | name    | email          | is_admin |
      | newUser  | newPassword | newName | some@email.com | false    |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When an user with username newUser and password newPassword tries to log in
    When try to get an user by username
    Then received status code is 200
    Then an user with username newUser is returned in response

  Scenario: An user is not authorized to get another user by username
    When try to create a new user with data
      | username | password    | name    | email          | is_admin |
      | newUser  | newPassword | newName | some@email.com | false    |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When an user with username newUser and password newPassword tries to log in
    Then received status code is 200
    When try to create a new user with data
      | username  | password      | name      | email           | is_admin |
      | otherUser | otherPassword | otherName | other@email.com | false    |
    Then received status code is 201
    Then an user with username otherUser has been stored successfully
    When try to get an user by username
    Then received status code is 403

  Scenario: Delete user by userId successfully
    When try to create a new user with data
      | username | password    | name    | email          | is_admin |
      | newUser  | newPassword | newName | some@email.com | true     |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When try to delete an user by userId
    Then received status code is 200
#    Then an entity User with id N/A has been stored into historical by user Admin and with type CREATE
#    Then an entity User with id N/A has been stored into historical by user Admin and with type DELETE
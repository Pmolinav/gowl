Feature: AuthApi

#  Example user: INSERT INTO public.users
# (user_id, creation_date, email, modification_date, "name", "password", username)
# VALUES(1, '2024-05-19 13:37:03.884', 'some@user.com', '2024-05-19 13:37:03.884', 'Some User',
# '$2a$10$pn85ACcwW6v74Kkt3pnPau7A4lv8N2d.fvwXuLsYanv07PzlXTu9S', 'someUser');

  Background:
    Given the following roles have been stored previously
      | role         |
      | ROLE_USER    |
      | ROLE_PREMIUM |
      | ROLE_ADMIN   |
    Given the following users have been stored previously
      | username | password     | name     | email          | roles      | creation_date | modification_date |
      | someUser | somePassword | someName | some@email.com | ROLE_ADMIN | 123456        | 123456            |

  Scenario: An user logs in successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200

  Scenario: Unauthorized login
    When an user with username otherUser and password otherPassword tries to log in
    Then received status code is 401

  Scenario: Create a new user successfully from public endpoint
    When try to create a new user with data with public endpoint
      | username | password    | name    | email         |
      | newUser  | newPassword | newName | new@email.com |
    Then received status code is 201
    Then an user with username newUser has been stored successfully

  Scenario: Create a new user from public endpoint bad request
    When try to create a new user with data with public endpoint
      | username | password    | email         |
      | newUser  | newPassword | new@email.com |
    Then received status code is 400

  Scenario: Get user by userId successfully from public endpoint
    When try to create a new user with data with public endpoint
      | username | password    | name    | email          |
      | newUser  | newPassword | newName | some@email.com |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When an user with username newUser and password newPassword tries to log in
    Then received status code is 200
    When try to get an user by userId with public endpoint
    Then received status code is 200
    Then an user with username newUser is returned in response

  Scenario: An user is not authorized to get other user by userId from public endpoint
    When try to create a new user with data with public endpoint
      | username | password    | name    | email          |
      | newUser  | newPassword | newName | some@email.com |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Then an user with username newUser has been stored successfully
    When try to get an user by userId with public endpoint
    Then received status code is 403

  Scenario: Get user by username successfully from public endpoint
    When try to create a new user with data with public endpoint
      | username | password    | name    | email          |
      | newUser  | newPassword | newName | some@email.com |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When an user with username newUser and password newPassword tries to log in
    When try to get an user by username with public endpoint
    Then received status code is 200
    Then an user with username newUser is returned in response

  Scenario: An user is not authorized to get another user by username from public endpoint
    When try to create a new user with data with public endpoint
      | username | password    | name    | email          |
      | newUser  | newPassword | newName | some@email.com |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When an user with username newUser and password newPassword tries to log in
    Then received status code is 200
    When try to get an user by username someUser with public endpoint
    Then received status code is 403

  Scenario: Delete user by userId successfully from public endpoint
    When try to create a new user with data with public endpoint
      | username | password    | name    | email          |
      | newUser  | newPassword | newName | some@email.com |
    Then received status code is 201
    When an user with username newUser and password newPassword tries to log in
    Then received status code is 200
    Then an user with username newUser has been stored successfully
    When try to delete an user by userId with public endpoint
    Then received status code is 200

  Scenario: An user is not authorized to delete another user from public endpoint
    When try to create a new user with data with public endpoint
      | username | password    | name    | email          |
      | newUser  | newPassword | newName | some@email.com |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Then an user with username newUser has been stored successfully
    When try to delete an user by userId with public endpoint
    Then received status code is 403
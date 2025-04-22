Feature: ActivityBOConfiguration

  Background:
    Given the following users have been stored previously
      | username | password     | name     | email          | role  | creation_date | modification_date |
      | someUser | somePassword | someName | some@email.com | ADMIN | 123456        | 123456            |
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200

  Scenario: Create a new activity unauthorized
    Given invalid auth token
    When try to create a new activity with data
      | name | description                          | price |
      | GYM  | Some description of the GYM activity | 520   |
    Then received status code is 401

  Scenario: Create a new activity successfully
    When try to create a new activity with data
      | name | description                          | price |
      | GYM  | Some description of the GYM activity | 520   |
    Then received status code is 201
    Then an activity with name GYM has been stored successfully
    Then an entity Activity with id GYM has been stored into historical by user Admin and with type CREATE

  Scenario: Create a new activity bad request
    When try to create a new activity with data
      | name | description                          | price |
      | GYM  | Some description of the GYM activity | 0     |
    Then received status code is 400

  Scenario: Get all activities successfully
    When try to create a new activity with data
      | name     | description                               | price |
      | GYM      | Some description of the GYM activity      | 520   |
      | FOOTBALL | Some description of the FOOTBALL activity | 1050  |
    Then received status code is 201
    Then an activity with name GYM has been stored successfully
    Then an activity with name FOOTBALL has been stored successfully
    When try to get all activities
    Then received status code is 200
    Then a list of activities with names GYM,FOOTBALL are returned in response
    Then an entity Activity with id GYM has been stored into historical by user Admin and with type CREATE
    Then an entity Activity with id FOOTBALL has been stored into historical by user Admin and with type CREATE

  Scenario: Get activity by activity name successfully
    When try to create a new activity with data
      | name | description                          | price |
      | GYM  | Some description of the GYM activity | 520   |
    Then received status code is 201
    Then an activity with name GYM has been stored successfully
    When try to get an activity by activity name
    Then received status code is 200
    Then an activity with name GYM is returned in response

  Scenario: Delete activity by activity name successfully
    When try to create a new activity with data
      | name | description                          | price |
      | GYM  | Some description of the GYM activity | 520   |
    Then received status code is 201
    Then an activity with name GYM has been stored successfully
    When try to delete an activity by activity name
    Then received status code is 200
    Then an entity Activity with id GYM has been stored into historical by user Admin and with type CREATE
    Then an entity Activity with id GYM has been stored into historical by user Admin and with type DELETE
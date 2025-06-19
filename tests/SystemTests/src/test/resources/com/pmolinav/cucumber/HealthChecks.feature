Feature: HealthChecks

  Background:
    Given the following roles have been stored previously
      | role         |
      | ROLE_USER    |
      | ROLE_PREMIUM |
      | ROLE_ADMIN   |
    Given the following users have been stored previously
      | username   | password       | name        | email            | roles      | creation_date | modification_date |
      | someUser   | somePassword   | someName    | some@email.com   | ROLE_ADMIN | 123456        | 123456            |
      | normalUser | normalPassword | Normal Name | normal@email.com | ROLE_USER  | 123456        | 123456            |
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200

  Scenario: Check health status UP
    When try to get AuthApi health
    Then received status code is 200
    When try to get UsersBOApi health
    Then received status code is 200
    When try to get LeaguesBOApi health
    Then received status code is 200
    When try to get LeagueApi health
    Then received status code is 200
    When try to get PredictionsBOApi health
    Then received status code is 200
    When try to get PredictionApi health
    Then received status code is 200

  Scenario: Check health status by unauthorized user
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 403
    When try to get AuthApi health
    Then received status code is 403
    When try to get UsersBOApi health
    Then received status code is 403
    When try to get LeaguesBOApi health
    Then received status code is 403
    When try to get LeagueApi health
    Then received status code is 403
    When try to get PredictionsBOApi health
    Then received status code is 403
    When try to get PredictionApi health
    Then received status code is 403

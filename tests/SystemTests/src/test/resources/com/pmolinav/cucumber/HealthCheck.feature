Feature: HealthCheck

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

  Scenario: Check health status UP
    When try to get health
    Then received status code is 200

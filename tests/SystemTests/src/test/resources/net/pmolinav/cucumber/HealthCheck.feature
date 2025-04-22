Feature: HealthCheck

  Scenario: Check health status UP
    When try to get health
    Then received status code is 200

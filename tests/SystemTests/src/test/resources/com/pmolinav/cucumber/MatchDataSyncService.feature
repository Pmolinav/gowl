Feature: MatchDataSyncService

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
    Given the following matches have been stored previously
      | match_id | category_id | season | match_day_number | home_team    | away_team  | start_time | status | creation_date | modification_date |
      | 101      | PREMIER     | 2025   | 1                | Team Alpha   | Team Beta  | 1624005000 | ACTIVE | 1624000000    | 1624005000        |
      | 102      | PREMIER     | 2025   | 1                | Team Gamma   | Team Delta | 1624006000 | ACTIVE | 1624212000    | 1624001000        |
      | 103      | PREMIER     | 2025   | 2                | Team Epsilon | Team Zeta  | 1624007000 | ACTIVE | 1624298400    | 1624002000        |
    Given the following events have been stored previously
      | event_type  | match_id | description       | creation_date | modification_date |
      | Goal        | 101      | Goal scored event | 1624000000    | 1624005000        |
      | Yellow Card | 101      | Yellow card event | 1624001000    | 1624006000        |
      | Red Card    | 102      | Red card event    | 1624002000    | 1624007000        |
    Given the following odds have been stored previously
      | odds_id | event_type  | label    | value | active | creation_date | modification_date |
      | 301     | Goal        | Home Win | 1.5   | true   | 1624000000    | 1624005000        |
      | 302     | Goal        | Away Win | 2.5   | true   | 1624001000    | 1624006000        |
      | 303     | Yellow Card | Over 2.5 | 1.8   | true   | 1624002000    | 1624007000        |
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200

   # SYNCHRONIZE
  Scenario: Synchronize matches from external API
    Given mock odds response from external API
    Then wait 130 seconds